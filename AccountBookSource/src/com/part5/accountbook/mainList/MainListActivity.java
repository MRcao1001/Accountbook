package com.part5.accountbook.mainList;

import com.part5.accountbook.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainListActivity extends FragmentActivity implements OnClickListener {

	// �ײ��˵�4��Linearlayout
    private LinearLayout ll_account;
    private LinearLayout ll_serachList;
    private LinearLayout ll_addList;
    private LinearLayout ll_user;

    // �ײ��˵�4��ImageView
    private ImageView iv_account;
    private ImageView iv_serachList;
    private ImageView iv_addList;
    private ImageView iv_user;

    //����������
    private TextView tv_title;
    // �ײ��˵�4���˵�����
    private TextView tv_account;
    private TextView tv_serachList;
    private TextView tv_addList;
    private TextView tv_user;

	// 4��Fragment
    private Fragment accountFragment;
    private Fragment serachFragment;
    private Fragment addAccountFragment;
    private Fragment userInfoFragment;
    
    //�û���Ϣ
    String userName;
    String homeNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        
        //��ʼ���ؼ�
        initView();
        
        //��ʼ���ײ���ť�¼�
        initEvent();
        
        //��ʼ�������õ�ǰFragement
        initFragment(0);
        
        //��ȡbundle��ֵ
        initGetData();
        
    }

    

	private  String initGetData() {
		// TODO Auto-generated method stub
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null){
			String dataStream = bundle.getString("userName");
			String userinfo[] = dataStream.split("_");
			userName = userinfo[0];
			homeNum = userinfo[1];
			return userName;
		}
		else{
			return "���¼/ע��";
		}
	}



	private void initFragment(int index) {
		// TODO Auto-generated method stub
		// ������������V4���µ�Fragment����������Ĺ�����Ҫ��getSupportFragmentManager��ȡ
        FragmentManager fragmentManager = getSupportFragmentManager();
        // ��������
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // ��������Fragment
        hideFragment(transaction);
        switch (index) {
        case 0:
            if (accountFragment == null) {
                accountFragment = new AccountFragment();
              //��fragment�д�ֵ
            	diliverValue(accountFragment);   
                transaction.add(R.id.fl_content, accountFragment);
            } else {
                transaction.show(accountFragment);
            }
            break;
        case 1:
            if (serachFragment == null) {
            	serachFragment = new SerachFragment();
                transaction.add(R.id.fl_content, serachFragment);
            } else {
                transaction.show(serachFragment);
            }

            break;
        case 2:
            if (addAccountFragment == null) {
            	addAccountFragment = new AddAccountFragment();
            	diliverValue(addAccountFragment);
                transaction.add(R.id.fl_content, addAccountFragment);
            } else {
                transaction.show(addAccountFragment);
            }

            break;
        case 3:
            if (userInfoFragment == null) {
            	userInfoFragment = new UserInfoFragment();
            	//��fragment�д�ֵ
            	diliverValue(userInfoFragment);           	
                transaction.add(R.id.fl_content, userInfoFragment);
            } else {
            	
                transaction.show(userInfoFragment);
                
            }

            break;

        default:
            break;
        }

        // �ύ����
        transaction.commit();

	}


//��ֵ����
	private void diliverValue(Fragment whichFragment) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
        bundle.putString("key", initGetData()+"_"+ homeNum);
        whichFragment.setArguments(bundle);
	}



	private void hideFragment(FragmentTransaction transaction) {
		// TODO Auto-generated method stub
		if (accountFragment != null) {
            transaction.hide(accountFragment);
        }
        if (serachFragment != null) {
            transaction.hide(serachFragment);
        }
        if (addAccountFragment != null) {
            transaction.hide(addAccountFragment);
        }
        if (userInfoFragment != null) {
            transaction.hide(userInfoFragment);
        }
	}



	private void initEvent() {
		// TODO Auto-generated method stub
		 // ���ð�ť����
        ll_account.setOnClickListener(this);
        ll_serachList.setOnClickListener(this);
        ll_addList.setOnClickListener(this);
        ll_user.setOnClickListener(this);
	}



	private void initView() {
		// TODO Auto-generated method stub
		// �ײ��˵�4��Linearlayout
        this.ll_account = (LinearLayout) findViewById(R.id.ll_account);
        this.ll_serachList = (LinearLayout) findViewById(R.id.ll_serachList);
        this.ll_addList = (LinearLayout) findViewById(R.id.ll_addList);
        this.ll_user = (LinearLayout) findViewById(R.id.ll_user);

        // �ײ��˵�4��ImageView
        this.iv_account = (ImageView) findViewById(R.id.iv_account);
        this.iv_serachList = (ImageView) findViewById(R.id.iv_serachList);
        this.iv_addList = (ImageView) findViewById(R.id.iv_addList);
        this.iv_user = (ImageView) findViewById(R.id.iv_user);

        // �ײ��˵�4���˵�����
        this.tv_account = (TextView) findViewById(R.id.tv_account);
        this.tv_serachList = (TextView) findViewById(R.id.tv_serachList);
        this.tv_addList = (TextView) findViewById(R.id.tv_addList);
        this.tv_user = (TextView) findViewById(R.id.tv_user);
        this.tv_title = (TextView) findViewById(R.id.tv_title);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// ��ÿ�ε�������еĵײ���ť(ImageView,TextView)��ɫ��Ϊ��ɫ��Ȼ����ݵ����ɫ
        restartBotton();
        // ImageView��TetxView��Ϊ��ɫ��ҳ����֮��ת
        switch (v.getId()) {
        case R.id.ll_account:
            iv_account.setImageResource(R.drawable.view_gallery_pressed);
            tv_title.setText("�˵�");
            initFragment(0);
            break;
        case R.id.ll_serachList:
            iv_serachList.setImageResource(R.drawable.search_pressed);
            tv_title.setText("����");
            initFragment(1);
            break;
        case R.id.ll_addList:
            iv_addList.setImageResource(R.drawable.edit_pressed);
            tv_title.setText("�����Ŀ");
            initFragment(2);
            break;
        case R.id.ll_user:
            iv_user.setImageResource(R.drawable.account_pressed);
            tv_title.setText("��");
            initFragment(3);
            break;

        default:
            break;
        }

	}
	

	private void restartBotton() {
		// TODO Auto-generated method stub
		 // ImageView��ԭ
		iv_account.setImageResource(R.drawable.view_gallery);
		iv_serachList.setImageResource(R.drawable.search);
		iv_addList.setImageResource(R.drawable.edit);
		iv_user.setImageResource(R.drawable.account);
	}



	//������ϵͳ�Զ����ɵĴ���
	//���ù���
	//���ù���
	//���ù���
	//���ù���
	//���ù���
	//���ù���
	//���ù���
	//���ù���
	//���ù���
	//���ù���
	//���ù���
	//���ù���
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        // super.onBackPressed();   ��Ҫ���ø���ķ���
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
