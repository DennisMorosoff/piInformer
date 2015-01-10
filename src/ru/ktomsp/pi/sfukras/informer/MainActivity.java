package ru.ktomsp.pi.sfukras.informer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	private SimpleAdapter mAdapter;
	private List<HashMap<String, String>> mList;
	final private String ITEM_MENU_TITLE = "title";
	final private String ITEM_MENU_ICON = "icon";
	final private String COUNT = "count";

	int[] mItemsIcons = new int[] { R.drawable.ic_action_event,
			R.drawable.ic_action_go_to_today, R.drawable.ic_action_play,
			R.drawable.ic_action_person, R.drawable.ic_action_group,
			R.drawable.ic_action_web_site, R.drawable.ic_action_settings };

	private DrawerLayout mMenuLayout; /* Слой меню */
	private ListView mMenuList; /* Список меню */
	private ActionBarDrawerToggle mMenuToggle; /* Переключатель меню */

	private CharSequence mMenuTitle; /* Заголовок меню */
	private CharSequence mTitle; /* Заголовок текущей страницы */
	private static String[] mPageTitles; /* Массив заголовков страниц (меню) */

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.d("myLogs", "onCreate start");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mMenuTitle = getTitle();
		mPageTitles = getResources().getStringArray(R.array.menu_array);
		mMenuLayout = (DrawerLayout) findViewById(R.id.menu_layout);
		mMenuList = (ListView) findViewById(R.id.left_menu);

		// устанавливаем тень поверх которой отобразится боковое меню
		mMenuLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mList = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < mPageTitles.length; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(ITEM_MENU_TITLE, mPageTitles[i]);
			hm.put(COUNT, " ");
			hm.put(ITEM_MENU_ICON, Integer.toString(mItemsIcons[i]));
			mList.add(hm);
		}

		String[] from = { ITEM_MENU_ICON, ITEM_MENU_TITLE, COUNT };

		int[] to = { R.id.menu_item_icon, R.id.menu_item_title, R.id.count };

		mAdapter = new SimpleAdapter(getActionBar().getThemedContext(), mList,
				R.layout.drawer_list_item, from, to);

		mMenuList.setAdapter(mAdapter);

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

				Log.d("myLogs", "onDrawerClosed start");

				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // вызываем onPrepareOptionsMenu()

				Log.d("myLogs", "onDrawerClosed finish");
			}

			public void onDrawerOpened(View drawerView) {

				Log.d("myLogs", "onDrawerOpened start");

				getActionBar().setTitle(mMenuTitle);
				invalidateOptionsMenu(); // вызываем onPrepareOptionsMenu()

				Log.d("myLogs", "onDrawerOpened finish");
			}
		};
		mMenuLayout.setDrawerListener(mMenuToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

		Log.d("myLogs", "onCreate finish");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		Log.d("myLogs", "onCreateOptionsMenu start");

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		Log.d("myLogs", "onCreateOptionsMenu finish");

		return super.onCreateOptionsMenu(menu);
	}

	/* вызывается всякий раз, когда мы вызываем invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		Log.d("myLogs", "onPrepareOptionsMenu start");

		// если боковое меню открыто прячем кнопку "Поиск"
		boolean drawerOpen = mMenuLayout.isDrawerOpen(mMenuList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);

		Log.d("myLogs", "onPrepareOptionsMenu finish");

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Log.d("myLogs", "onOptionsItemSelected start");

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

			Log.d("myLogs", "onOptionsItemSelected finish");

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

			Log.d("myLogs", "onItemClick start");

			selectItem(position);

			Log.d("myLogs", "onItemClick finish");

		}
	}

	private void selectItem(int position) {

		Log.d("myLogs", "selectItem start");

		// обновляем фрагмент на основном экране
		Fragment fragment = new MainPagesFragment(mPageTitles);
		Bundle args = new Bundle();
		args.putInt(MainPagesFragment.ARG_MENU_ITEM_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// обновляем выбранный элемент меню, заговолок и закрываем боковое меню
		mMenuList.setItemChecked(position, true);
		setTitle(mPageTitles[position]);
		mMenuLayout.closeDrawer(mMenuList);

		Log.d("myLogs", "selectItem finish");
	}

	@Override
	public void setTitle(CharSequence title) {

		Log.d("myLogs", "setTitle start");

		mTitle = title;
		getActionBar().setTitle(mTitle);

		Log.d("myLogs", "setTitle finish");

	}

	/**
	 * поскольку используется ActionBarDrawerToggle, нужно переопределить
	 * onPostCreate() и onConfigurationChanged()
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		Log.d("myLogs", "onPostCreate start");

		super.onPostCreate(savedInstanceState);
		mMenuToggle.syncState();

		Log.d("myLogs", "onPostCreate finish");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		Log.d("myLogs", "onConfigurationChanged start");

		super.onConfigurationChanged(newConfig);
		mMenuToggle.onConfigurationChanged(newConfig);

		Log.d("myLogs", "onConfigurationChanged finish");
	}
}