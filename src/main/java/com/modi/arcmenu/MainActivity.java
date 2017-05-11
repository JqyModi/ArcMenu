package com.modi.arcmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;
	private ImageView iv4;
	private ArcMenu amMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
		initEvent();

	}

	private void initView() {

		amMenu = (ArcMenu) findViewById(R.id.am_menu);
		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv1);
		iv3 = (ImageView) findViewById(R.id.iv1);
		iv4 = (ImageView) findViewById(R.id.iv1);

		//
	}

	private void initData() {

	}

	private void initEvent() {
		amMenu.setmItemClickListener(new ArcMenu.ItemClickListener() {
			@Override
			public void onItemClick(View v, int position) {
				//
				int id = v.getId();
				if (id == R.id.iv1){
					Toast.makeText(MainActivity.this,"音乐",Toast.LENGTH_SHORT).show();
				}else if (id == R.id.iv2){
					Toast.makeText(MainActivity.this,"视频",Toast.LENGTH_SHORT).show();
				}else if (id == R.id.iv3){
					Toast.makeText(MainActivity.this,"相机",Toast.LENGTH_SHORT).show();
				}else if (id == R.id.iv4){
					Toast.makeText(MainActivity.this,"游戏",Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onItemLongClick(View v, int position) {

			}
		});
	}
}
