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
 * ��������, ������� ���� ��� ����� ������ ���������� �������� ��������� ����
 */
public class MainPagesFragment extends Fragment {

	public static final String ARG_MENU_ITEM_NUMBER = "menu_item_number";

	public MainPagesFragment() {

		Log.d("myLogs", "MainPageFragment start");

		// ��� ���������� ��������� ����� ������������ �����������, ���� ��
		// ���� ������

		Log.d("myLogs", "MainPageFragment finish");

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

		// �������� ����� ���������� �������� ����
		int i = getArguments().getInt(ARG_MENU_ITEM_NUMBER);

		Log.d("myLogs", "i = " + i);

		// �� ������ ���������� �������� ���� �������� ��� ����� �������
		// ��������������� ����� �������� ����
		String pages = getResources().getStringArray(R.array.menu_array_images)[i];

		Log.d("myLogs", "pages = " + pages);

		// �� ����� �������� �������� ����� ������� � ��������
		int imageId = getResources().getIdentifier(
				pages.toLowerCase(Locale.getDefault()), "drawable",
				getActivity().getPackageName());

		Log.d("myLogs", "imageId: " + imageId + ", R.id.image: " + R.id.image);

		Log.d("myLogs", "getActivity(): " + getActivity());

		// �������� ��������� �� ��������� - ImageView
		 ImageView mPageImage = (ImageView) rootView.findViewById(R.id.image);

		MainActivity mActivity = (MainActivity) getActivity();
		
		Log.d("myLogs", "mPageImage: " + mPageImage + ", mPageTitles[0]: "
				+ mActivity.mPageTitles[i]);

		// ������������� ����� ��������� ����
		getActivity().setTitle(mActivity.mPageTitles[i]);

		Log.d("myLogs", "imageId: " + imageId);

		// ��������� ������� � ��������� � ��������� ������
		loadImage(mPageImage, imageId);

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