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
	private DrawerLayout mMenuLayout; /* ���� ���� */
	private ListView mMenuList; /* ������ ���� */
	private ActionBarDrawerToggle mMenuToggle; /* ������������� ���� */

	private CharSequence mMenuTitle; /* ��������� ���� */
	private CharSequence mTitle; /* ��������� ������� �������� */
	private static String[] mPageTitles; /* ������ ���������� ������� (����) */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mMenuTitle = getTitle();
		mPageTitles = getResources().getStringArray(R.array.menu_array);
		mMenuLayout = (DrawerLayout) findViewById(R.id.menu_layout);
		mMenuList = (ListView) findViewById(R.id.left_menu);

		// ������������� ���� ������ ������� ����������� ������� ����
		mMenuLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// ��������� mMenuList ���������� ����
		mMenuList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPageTitles));
		// ������������� ��������� �� ������ ����
		mMenuList.setOnItemClickListener(new DrawerItemClickListener());

		// �������� ������ "�����", ������� ����� ����������� �����������
		// �������� ����
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ������ ��� ���������� ������ �� ������ ��������
		// (ActionBar) �
		// ����������� �������� ���� (Drawer) �� ������ ������
		mMenuToggle = new ActionBarDrawerToggle(this, /*
													 * ���� ��������� � ����
													 * ����������
													 */
		mMenuLayout, /* ���� ���� */
		R.drawable.ic_drawer, /*
							 * ������ ����� ������ ��� ��������, ��� ���������
							 * ������� � ���������� �������� ����
							 */
		R.string.menu_open, /* �������� �������� ���� */
		R.string.menu_close /* �������� �������� ���� */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // �������� onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mMenuTitle);
				invalidateOptionsMenu(); // �������� onPrepareOptionsMenu()
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

	/* ���������� ������ ���, ����� �� �������� invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// ���� ������� ���� ������� ������ ������ "�����"
		boolean drawerOpen = mMenuLayout.isDrawerOpen(mMenuList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// ������ "�����" ������ ���������/��������� ����
		// ActionBarDrawerToggle ����������� �� ����.
		if (mMenuToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// ������������ ������ ���� ������ ��������
		switch (item.getItemId()) {
		case R.id.action_websearch:
			// ������ ��������� ��� ������ ������ � ��� ���������� �� ���������
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			// ���� ��� ������� ������ ��������� �������
			// "��������������� �������� ���"
			intent.putExtra(SearchManager.QUERY, "��������������� �������� ���");
			// ��������� �������, ������� ��������� ������
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

	/* ������������ ������ �� �������� �������� ���� */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// ��������� �������� �� �������� ������
		Fragment fragment = new MainPageFragment();
		Bundle args = new Bundle();
		args.putInt(MainPageFragment.ARG_MENU_ITEM_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// ��������� ��������� ������� ����, ��������� � ��������� ������� ����
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
	 * ��������� ������������ ActionBarDrawerToggle, ����� ��������������
	 * onPostCreate() � onConfigurationChanged()
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
	 * ��������, ������� ���� ��� ����� ������ ���������� �������� ���������
	 * ����
	 */
	public static class MainPageFragment extends Fragment {
		public static final String ARG_MENU_ITEM_NUMBER = "menu_item_number";

		public MainPageFragment() {
			// ��� ���������� ��������� ����� ������������ �����������, ���� ��
			// ���� ������
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