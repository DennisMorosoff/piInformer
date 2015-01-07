package ru.ktomsp.pi.sfukras.informer;

import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DrawerLayout mMenuLayout; /* Слой меню */
	private ListView mMenuList; /* Список меню */
	private ActionBarDrawerToggle mMenuToggle; /* Переключатель меню */

	private CharSequence mMenuTitle; /* Заголовок меню */
	private CharSequence mTitle; /* Заголовок текущей страницы */
	private static String[] mPageTitles; /* Массив заголовков страниц (меню) */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mMenuTitle = getTitle();
		mPageTitles = getResources().getStringArray(R.array.menu_array);
		mMenuLayout = (DrawerLayout) findViewById(R.id.menu_layout);
		mMenuList = (ListView) findViewById(R.id.left_menu);

		// устанавливаем тень поверх которой отобразится боковое меню
		mMenuLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// заполняем mMenuList элементами меню
		mMenuList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPageTitles));
		// устанавливаем слушателя на список меню
		mMenuList.setOnItemClickListener(new DrawerItemClickListener());

		// включаем иконку "Домой", которая будет переключать отображение
		// бокового меню
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle служит для связывания кнопки на панели действий
		// (ActionBar) и
		// выпадающего бокового меню (Drawer) по свайпу вправо
		mMenuToggle = new ActionBarDrawerToggle(this, /*
													 * меню привязано к этой
													 * активности
													 */
		mMenuLayout, /* слой меню */
		R.drawable.ic_drawer, /*
							 * ставим слева вверху три чёрточки, как индикатор
							 * наличия в приложении бокового меню
							 */
		R.string.menu_open, /* описание открытия меню */
		R.string.menu_close /* описание закрытия меню */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // вызываем onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mMenuTitle);
				invalidateOptionsMenu(); // вызываем onPrepareOptionsMenu()
			}
		};
		mMenuLayout.setDrawerListener(mMenuToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* вызывается всякий раз, когда мы вызываем invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// если боковое меню открыто прячем кнопку "Поиск"
		boolean drawerOpen = mMenuLayout.isDrawerOpen(mMenuList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// кнопка "Домой" должна открывать/закрывать меню
		// ActionBarDrawerToggle позаботится об этом.
		if (mMenuToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// обрабатываем пункты меню панели действий
		switch (item.getItemId()) {
		case R.id.action_websearch:
			// создаём намерение для вызова поиска в веб информации об институте
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			// пока что выводит только результат запроса
			// "Политехнический институт СФУ"
			intent.putExtra(SearchManager.QUERY, "Политехнический институт СФУ");
			// запускаем браузер, выводим результат поиска
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* обрабатываем щелчок по элементу бокового меню */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// обновляем фрагмент на основном экране
		Fragment fragment = new MainPageFragment();
		Bundle args = new Bundle();
		args.putInt(MainPageFragment.ARG_MENU_ITEM_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// обновляем выбранный элемент меню, заговолок и закрываем боковое меню
		mMenuList.setItemChecked(position, true);
		setTitle(mPageTitles[position]);
		mMenuLayout.closeDrawer(mMenuList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * поскольку используется ActionBarDrawerToggle, нужно переопределить
	 * onPostCreate() и onConfigurationChanged()
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mMenuToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mMenuToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Фрагмент, который пока что умеет только отображать картинки элементов
	 * меню
	 */
	public static class MainPageFragment extends Fragment {
		public static final String ARG_MENU_ITEM_NUMBER = "menu_item_number";

		public MainPageFragment() {
			// для подклассов фрагмента нужен обязательный конструктор, хотя бы
			// даже пустой
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_pages,
					container, false);
			int i = getArguments().getInt(ARG_MENU_ITEM_NUMBER);
			String pages = getResources().getStringArray(R.array.menu_array_images)[i];
			

			int imageId = getResources().getIdentifier(
					pages.toLowerCase(Locale.getDefault()), "drawable",
					getActivity().getPackageName());
			((ImageView) rootView.findViewById(R.id.image))
					.setImageResource(imageId);
			getActivity().setTitle(mPageTitles[i]);
			return rootView;
		}
	}
}