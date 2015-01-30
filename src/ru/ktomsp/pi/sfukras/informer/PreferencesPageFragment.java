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
 * Фрагмент, который содержит настройки
 */
public class PreferencesPageFragment extends Fragment {

	public PreferencesPageFragment() {

		Log.d("myLogs", "PreferencesPageFragment start");

		// для подклассов фрагмента нужен обязательный конструктор, хотя бы
		// даже пустой

		Log.d("myLogs", "PreferencesPageFragment finish");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d("myLogs", "onCreateView start");
		// получаем указатель на корневой компонент слоя fragment_pages. В
		// данном случае это ImageView
		View rootView = inflater.inflate(R.layout.fragment_pages, container,
				false);

		Log.d("myLogs", "rootView: " + rootView);


		// получаем указатель на компонент - ImageView
		ImageView mPageImage = (ImageView) rootView.findViewById(R.id.image);

		MainActivity mActivity = (MainActivity) getActivity();

		Log.d("myLogs", "mPageImage: " + mPageImage + ", mPageTitles[6]: "
				+ mActivity.mPageTitles[6]);

		// устанавливаем новый заголовок окна
		getActivity().setTitle(mActivity.mPageTitles[6]);

		Log.d("myLogs", "imageId: " + R.drawable.prefs);

		// загружаем рисунок в компонент в отдельном потоке
		loadImage(mPageImage, R.drawable.prefs);

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