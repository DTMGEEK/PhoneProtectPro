package com.example.androiddefprot.ui;


import java.io.File;
import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.UpdateInfo;
import com.example.androiddefprot.engine.DownLoadFileTask;
import com.example.androiddefprot.engine.UpdateInfoService;
import com.example.androiddefprot.logmanagement.LogManagement;
import com.example.androiddefprot.util.NetUtils;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 *
 * @Title: SplashActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: Splahs界面
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class SplashActivity extends ActionBarActivity {

    private TextView tv_splash_version = null;
    private LinearLayout ll_splash_main = null;
    private UpdateInfo updateInfo = null; //保存更新信息
    private static final String TAG = "SplashActivity";
    private  String version = null;        //
    private ProgressDialog pd = null;
    private Handler handler = null;
    private NetUtils nu = null;
    private boolean isConned2Net = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //除去标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        nu = new NetUtils(SplashActivity.this);
       // this.tv_splash_version = (TextView) this.findViewById(R.id.tv_splash_version);
        this.ll_splash_main = (LinearLayout) this.findViewById(R.id.ll_splash_main);
        this.pd = new ProgressDialog(this);
        this.pd.setMessage("正在下载。。。。");
        this.pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        version = getVersion();

//        this.tv_splash_version.setText(version);
        AlphaAnimation animation = new AlphaAnimation(0.0f,1.0f);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                if(nu.isConnect2NetWork()){
                    //判断本地版本号 和客户端版本号是否相同
                    isNeedUpdate();
                }else{
                   go2Setting();
                }
            }



            @Override
            public void onAnimationEnd(Animation animation) {

                    loadMainScreen();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animation.setDuration(6000);
        ll_splash_main.setAnimation(animation);
        animation.start();


        //isNeedUpdate();

        //设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /**
     * 当有新版本的时候，弹出窗口 询问是否更新apk
     */
    private void showUpdateDialog() {
        Dialog dialog = new AlertDialog.Builder(this).setCancelable(false)//用户不能取消对话框
                .setIcon(R.drawable.icon5).setTitle("升级提醒")
                .setMessage(updateInfo.getDescription())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogManagement.i(TAG, "更新apk");
                        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                            pd.show();
                            new DownLaodAPKThreadS(updateInfo.getApkurl(),"/sdcard/new.apk").execute("test");
                            //new Thread(new DownLaodAPKThread(updateInfo.getApkurl(),"/sdcard/new.apk")).start();
                        }else{
                            Toast.makeText(SplashActivity.this, "SD卡不可用", Toast.LENGTH_LONG).show();
                            SplashActivity.this.loadMainScreen();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogManagement.i(TAG, "用户取消 进入程序主界面");
                        SplashActivity.this.loadMainScreen();

                    }
                }).create();
        dialog.show();
    }



    /**
     *
     * @return 是否需要更新
     */
    private void isNeedUpdate() {
        new Thread(new ThreadTe()).start();
        this.handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                SplashActivity.this.updateInfo = (UpdateInfo) msg.obj;
                if(version.equals(updateInfo.getVersion())){
                    LogManagement.i(TAG, "版本号一致，无需升级");
                    //Toast.makeText(SplashActivity.this, "版本号一致，无需升级", Toast.LENGTH_LONG).show();
                    SplashActivity.this.loadMainScreen();
                }else{
                    LogManagement.i(TAG, "版本号不一致");
                    //Toast.makeText(SplashActivity.this, "版本号不一致", Toast.LENGTH_LONG).show();
                    showUpdateDialog();
                }
            }
        };
    }

	
	/*private boolean isNeedUpdate(String versiontext){
		UpdateInfoService service = new UpdateInfoService(this);
		try {
			updateInfo = service.getUpdateInfo(R.string.apkurl);
			String version = updateInfo.getVersion();
			if(versiontext.equals(version)){
				LogManagement.i(TAG, "版本相同,无需升级");
				SplashActivity.this.loadMainScreen();
				return false;
			}else{ 
				LogManagement.i(TAG, "版不同，需要升级");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "获取更新信息异常", 0).show();
			LogManagement.i(TAG, "获取更新信息异常, 进入主界面");
			SplashActivity.this.loadMainScreen();
			return false;
		}
	}*/


    /**、
     * 多线程类，负责在后台开启一个新的线程和服务器进行通信。判断服务器版本号是否和本地版本号一致（通过继承AsyncTask 类实现）
     * @author jake
     *
     */

    class getServiceVersionThread extends AsyncTask<Integer, String, UpdateInfo>{

        @Override
        protected void onPostExecute(UpdateInfo result) {
            super.onPostExecute(result);
            if(SplashActivity.this.version.equals(result.getVersion())){
                LogManagement.i(TAG, "版本号一致，无需升级");
                Toast.makeText(SplashActivity.this, "版本号一致，无需升级", Toast.LENGTH_LONG).show();
            }else{
                LogManagement.i(TAG, "版本号不一致");
                Toast.makeText(SplashActivity.this, "版本号不一致", Toast.LENGTH_LONG).show();
                showUpdateDialog();
            }
        }

        @Override
        protected UpdateInfo doInBackground(Integer... params) {
            UpdateInfoService service = new UpdateInfoService(SplashActivity.this);
            UpdateInfo updateinfo = null;
            try {
                updateinfo = service.getUpdateInfo(R.string.apkurl);
                LogManagement.i("SplashActivity", updateinfo.getVersion());

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return updateinfo;
        }
    }



    /**
     * 多线程类，负责在后台开启一个新的线程和服务器进行通信。判断服务器版本号是否和本地版本号一致（通过继承Runnable接口 实现）
     * @author jake
     *
     */
    class ThreadTe implements Runnable{
        @Override
        public void run() {
            UpdateInfoService service = new UpdateInfoService(SplashActivity.this);
            try {
                UpdateInfo updateinfo = service.getUpdateInfo(R.string.apkurl);

                Message msg = new Message();
                msg.obj = updateinfo;
                SplashActivity.this.handler.sendMessage(msg);
                //SplashActivity.this.handler.sendMessageDelayed(msg, 1000);

            } catch (Exception e) {
                e.printStackTrace();
                SplashActivity.this.loadMainScreen();
            }
        }
    }


    /**
     * 获得版本号
     * @return
     */
    private String getVersion(){
        String version = null;
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return this.getResources().getString(R.string.unknow_verson);
    }


    /**
     * 加载进入程序主界面
     */
    private void loadMainScreen(){
        Intent intent = new Intent(this,MainScreenActivity.class);
        this.startActivity(intent);
        this.finish();//把Splash的Activity从栈中移除，防止用户按回退按钮 后再出现Splash界面
    }




    /**
     * 启动一个新的线程,下载新的apk文件 并安装(方法1 继承Runnable接口)
     * @author jake
     *
     */
    private class DownLaodAPKThread implements Runnable{

    	private String path;//服务器uri
    	private String filePath; //文件路径
    	
		public DownLaodAPKThread(String path, String filePath) {
			super();
			this.path = path;
			this.filePath = filePath;
		}

		@Override
		public void run() {
			try {
				//Looper.prepare();
				File file = DownLoadFileTask.getFile(path, filePath, pd);
				LogManagement.i(SplashActivity.this.TAG, "下载完成");
				SplashActivity.this.pd.dismiss();
	
				SplashActivity.this.install(file);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogManagement.i(SplashActivity.this.TAG, "下载错误");
				Toast.makeText(SplashActivity.this, "下载错误", Toast.LENGTH_LONG).show();
				pd.dismiss();
				SplashActivity.this.loadMainScreen();
			}
		}
    }

    /**
     * 启动一个新的线程,下载新的apk文件 并安装(方法2 继承AsyncTask<String, String, File>)
     * @author jake
     *
     */
    private class DownLaodAPKThreadS extends AsyncTask<String, String, File>{

        private String path;
        private String filePath;


        public DownLaodAPKThreadS(String path, String filePath) {
            super();
            this.path = path;
            this.filePath = filePath;
        }


        @Override
        protected void onPostExecute(File result) {
            //LogManagement.i(SplashActivity.this.TAG,result.toString());
            SplashActivity.this.pd.dismiss();
            SplashActivity.this.install(result);
        }


        @Override
        protected File doInBackground(String... params) {
            File file = null;
            try {

                file = DownLoadFileTask.getFile(path, filePath, pd);
                LogManagement.i(SplashActivity.this.TAG, "下载完成");
                SplashActivity.this.pd.dismiss();
                SplashActivity.this.install(file);
            } catch (Exception e) {
                e.printStackTrace();
                LogManagement.i(SplashActivity.this.TAG, "下载错误");
                pd.dismiss();
                SplashActivity.this.loadMainScreen();
            }
            return file;
        }
    }



    /**
     * 下载完成新的apk后 自动安装
     */
    public void install(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        this.startActivity(intent);
        this.finish();
    }





    /**
     * 没有链接网络的时候 进入设置
     */
    private void go2Setting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("提示");
        builder.setMessage("没链接网络，请按确定打开网络");

        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nu.goToSetting();
            }
        });

        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadMainScreen();
            }
        });


        builder.create().show();
    }


}
