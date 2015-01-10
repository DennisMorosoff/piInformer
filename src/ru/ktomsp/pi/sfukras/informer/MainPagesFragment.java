package ru.ktomsp.pi.sfukras.informer;

import java.util.Locale;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

	/**
	 * Фрагмент, который пока что умеет только отображать картинки элементов
	 * меню
	 */
	public class MainPagesFragment extends Fragment {
		private static String[] mPageTitles; /* Массив заголовков страниц (меню) */
		public static final String ARG_MENU_ITEM_NUMBER = "menu_item_number";

		public MainPagesFragment(String[] mPageTitles) {

			Log.d("myLogs", "MainPageFragment start");

			// для подклассов фрагмента нужен обязательный конструктор, хотя бы
			// даже пустой
			
			Log.d("myLogs", "R.array.menu_array: " + R.array.menu_array);
			
//			mPageTitles = getResources().getStringArray(R.array.menu_array);
			
			Log.d("myLogs", "mPageTitles: " + mPageTitles);			

			Log.d("myLogs", "MainPageFragment finish");

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			Log.d("myLogs", "onCreateView start");
			// получаем указатель на корневой компонент слоя fragment_pages. В данном случае это
			// ImageView
			View rootView = inflater.inflate(R.layout.fragment_pages,
					container, false);

			Log.d("myLogs", "rootView: " + rootView);

			// получаем номер выбранного элемента меню
			int i = getArguments().getInt(ARG_MENU_ITEM_NUMBER);

			Log.d("myLogs", "i = " + i);

			// по номеру выбранного элемента меню получаем имя файла рисунка
			// соответствующей этому элементу меню
			String pages = getResources().getStringArray(
					R.array.menu_array_images)[i];

			Log.d("myLogs", "pages = " + pages);

			// по имени картинки получаем номер рисунка в ресурсах
			int imageId = getResources().getIdentifier(
					pages.toLowerCase(Locale.getDefault()), "drawable",
					getActivity().getPackageName());

			Log.d("myLogs", "imageId: " + imageId + ", R.id.image: "
					+ R.id.image);
			
			Log.d("myLogs", "getActivity(): " + getActivity());

			// получаем указатель на компонент - ImageView
			final ImageView mPageImage = (ImageView) getActivity().findViewById(R.id.image);
			
	//		final ImageView mPageImage = (ImageView) rootView;

			Log.d("myLogs", "mPageImage: " + mPageImage + ", mPageTitles[i]: "
					+ mPageTitles[i]);

			// устанавливаем новый заголовок окна
			getActivity().setTitle(mPageTitles[i]);

			Log.d("myLogs", "imageId: " + imageId);

			// загружаем рисунок в компонент в отдельном потоке
			loadImage(mPageImage, imageId);

			Log.d("myLogs", "onCreateView finish");

			return rootView;
		}

		private void loadImage(final ImageView mPageImage, final int imageId) {
			// загружаем рисунок в компонент в отдельном потоке
			mPageImage.post(new Runnable() {
				@Override
				public void run() {
					mPageImage.setImageResource(imageId);
				}
			});
		}
	}