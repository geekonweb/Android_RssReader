package com.lgq.rssreader;

import com.lgq.rssreader.task.DownloadService;
import com.lgq.rssreader.utils.Helper;
import com.lgq.rssreader.utils.NetHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class BaseMainActivity extends BaseActivity {
	private static final int DIALOG_OFFLINE_DOWNLOAD_GUID = 0;// ��������
	private AlertDialog dialogOfflineDownload;// �Ի���
	
	

	public boolean IsShowQuitHints=true;
	/**
	 * ���¼����Ϸ��ذ�ť
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && IsShowQuitHints) {//����
			Helper.QuitHintDialog(this);

			return true;
		}else if(keyCode==KeyEvent.KEYCODE_SEARCH){//����
			Intent intent = new Intent(BaseMainActivity.this,SearchActivity.class);
			intent.putExtra("isShowQuitHints", false);
			startActivity(intent);
			return true;
		}else {		
			return super.onKeyDown(keyCode, event);
		}
	}
		
	
	/**
	 * �����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//this.getMenuInflater().inflate(R.menu.main_tab_menu, menu);
		return true;
	}
	
//	/**
//	 * ��ת������
//	 */
//	private void RedirectAboutActivity() {
//		Intent intent = new Intent();
//		intent.setClass(getApplicationContext(), AboutActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putInt("fromActivity", 0);
//		intent.putExtras(bundle);
//
//		startActivity(intent);
//	}
//	/**
//	 * ��ת 
//	 */
//	private void RedirectSettingActivity() {
//		Intent intent = new Intent();
//		intent.setClass(getApplicationContext(), SettingActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putInt("fromActivity", 0);
//		intent.putExtras(bundle);
//
//		startActivity(intent);
//	}
//	private void RedirectMyFavActivity(){
//
//		Intent intent = new Intent();
//		intent.setClass(getApplicationContext(), MyFavActivity.class);
//
//		startActivity(intent);
//	}
}
