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
 * ��������, ������� �������� ���������
 */
public class PreferencesPageFragment extends Fragment {

	public PreferencesPageFragment() {

		Log.d("myLogs", "PreferencesPageFragment start");

		// ��� ���������� ��������� ����� ������������ �����������, ���� ��
		// ���� ������

		Log.d("myLogs", "PreferencesPageFragment finish");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d("myLogs", "onCreateView start");
		// �������� ��������� �� �������� ��������� ���� fragment_pages. �
		// ������ ������ ��� ImageView
		View rootView = inflater.inflate(R.layout.fragment_pages, container,
				false);

		Log.d("myLogs", "rootView: " + rootView);


		// �������� ��������� �� ��������� - ImageView
		ImageView mPageImage = (ImageView) rootView.findViewById(R.id.image);

		MainActivity mActivity = (MainActivity) getActivity();

		Log.d("myLogs", "mPageImage: " + mPageImage + ", mPageTitles[6]: "
				+ mActivity.mPageTitles[6]);

		// ������������� ����� ��������� ����
		getActivity().setTitle(mActivity.mPageTitles[6]);

		Log.d("myLogs", "imageId: " + R.drawable.prefs);

		// ��������� ������� � ��������� � ��������� ������
		loadImage(mPageImage, R.drawable.prefs);

		Log.d("myLogs", "onCreateView finish");

		return rootView;
	}

	private void loadImage(final ImageView mPageImage, final int imageId) {
		// ��������� ������� � ��������� � ��������� ������
		mPageImage.post(new Runnable() {
			@Override
			public void run() {
				mPageImage.setImageResource(imageId);
			}
		});
	}
}