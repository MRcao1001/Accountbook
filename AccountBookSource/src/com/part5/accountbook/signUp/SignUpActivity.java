package com.part5.accountbook.signUp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.part5.accountbook.MainActivity;
import com.part5.accountbook.R;
import com.part5.accountbook.mainList.MainListActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SignUpActivity extends Activity {
	//����״̬���������Ƕ����ʧ�ܾ͸�ֵΪerror
	String netWork = "error";
	private TextView cancle;
	boolean status	;
	//���ر�ǩ
	private LinearLayout ll_back;
	//��¼��ť
	private Button bt_signUp;
	//��ֵ����
	public String type;
	//���������ı���
	private EditText et_homeNum;
	private EditText et_userName;
	private EditText et_signPwd1;
	private EditText et_signPwd2;
	//����TCPЭ���������Ķ���
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    private TextView mTextView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //ʵ�������������
        et_homeNum = (EditText)findViewById(R.id.et_signhome);
        et_userName = (EditText)findViewById(R.id.et_signuserName);
        et_signPwd1 = (EditText)findViewById(R.id.et_signPwd1);
        et_signPwd2 = (EditText)findViewById(R.id.et_signPwd2);
        //ȡ��ע�ᰴť
        cancle = (TextView)findViewById(R.id.cancle);
        cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
      //��� idΪ ll_back �ı�ǩʱ������һ������
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        //���ע�ᰴť
        bt_signUp = (Button)findViewById(R.id.bt_signUp);
        mTextView1 = (TextView)findViewById(R.id.myTextView2);
        bt_signUp.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mTextView1.setText("�������ӵ�������...");
				mTextView1.setBackgroundColor(Color.parseColor("#3572bd"));
				//������ͨ���ж�
				//����ͨ��
				if(ping()){
					//�����п�
					if(et_homeNum.getText().toString().trim().isEmpty())
					{
						mTextView1.setText("��ͥ�Ų���Ϊ��");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(et_userName.getText().toString().trim().isEmpty())
					{
						mTextView1.setText("�û�������Ϊ��");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(et_signPwd1.getText().toString().trim().isEmpty())
					{
						mTextView1.setText("���벻��Ϊ��");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					//����������ͬ������
					if(!(   et_signPwd1.getText().toString().equals(   et_signPwd2.getText().toString()   )   )   )
					{
						mTextView1.setText("��������ͬ������");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					//�ж��Ƿ�����»���
					if(status = et_homeNum.getText().toString().contains("_")){
						mTextView1.setText("���벻�ɰ����»���");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(status = et_userName.getText().toString().contains("_")){
						mTextView1.setText("���벻�ɰ����»���");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(status = et_signPwd1.getText().toString().contains("_")){
						mTextView1.setText("���벻�ɰ����»���");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					//ͨ�������ж�
					mTextView1.setText("���ڳ���ע��...");
					mTextView1.setBackgroundColor(Color.parseColor("#35bd4b"));
					SignUp();
    	        	return;
				}
				//������
				Toast.makeText(SignUpActivity.this, "�����쳣��������������", Toast.LENGTH_LONG).show();
				return;
			}
		});
        
    }
    
    
    public void SignUp() {
		// TODO Auto-generated method stub
		//ʵ��Activity֮�䴫ֵ
		final Intent intent_signUp = new Intent();
		intent_signUp.setClass(SignUpActivity.this, MainListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("userName", et_userName.getText().toString()+"_"+ et_homeNum.getText().toString());
		intent_signUp.putExtras(bundle);
        //����handle�������̺߳����߳�ͨ��
		final Handler mHandler = new Handler(){  
      	  
    	    @Override  
    	    public void handleMessage(Message msg) {  
    	        super.handleMessage(msg);  
    	        //����ɹ�������socket��������²���
    	        if(netWork.equals("great")){
    	        	switch (msg.what) {  
        	        //�ж��˺����벢����������
        	        case 0:
        	        	String data = (String)msg.obj;
        	        	String key = "OK";
        	            if(data.equals(key)){
        	            	mTextView1.setText("ע��ɹ���");
        	            	mTextView1.setBackgroundColor(Color.parseColor("#35bd4b"));
        	            	startActivityForResult(intent_signUp, 0);
        	            }
        	            else if(data.equals("filed")){
        	            	mTextView1.setText("�û��ѱ�ע��");
        	            	mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
        	            }
        	            break;  
        	        default:  
        	            break;  
        	        } 
    	        }
    	        //û�����ӵ�������
    	        else
    	        {
    	        	return;
    	        }
    	        	
    	         
    	    }  
    	      
    	};
    	//client ���½�һ���̣߳�����socketǶ�������ӵ�4321
		new Thread() {
			   public void run(){

				   		try{
						
						socket = new Socket("193.112.137.246",9999);
						netWork = "great";
							//193.112.137.246---172.18.126.68
						}
						catch(Exception e){
							System.out.print("socket err");
							return;
						}
						//������������������ȡ����
						try {
							dis = new DataInputStream(socket.getInputStream());
							dos = new DataOutputStream(socket.getOutputStream());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.print("DataStream create err");
						}
						
						try {
							//Thread.sleep(2000);
							String str = et_homeNum.getText().toString()+"_"+ et_userName.getText().toString()+"_"+ et_signPwd1.getText().toString();
							WriteString(str);
							
						} catch (Exception e) {
							// TODO: handle exception
							System.out.print("socket clsoe err");
						}

						//��ȡ����
						
						try {
							Thread.sleep(3000);
							ReadStr();
							socket.close();
							Message message = new Message();
				            message.obj = type;
				            mHandler.sendMessage(message);
						} catch (Exception e) {
							// TODO: handle exception
							System.out.print("socket clsoe err");
						}
						
						
						
					}
			   
			}.start();
			 		
	}
	public void WriteString(String str) {
		// TODO Auto-generated method stub
		try {
			dos.writeUTF(str);
			System.out.print("Read OK1");
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("write err");
		}
	}
	public void ReadStr() {
		// TODO Auto-generated method stub
		try {
			dis = new DataInputStream(socket.getInputStream());
			type = dis.readUTF();
			dis.close();
			dos.close();
//			if(type != null){
//				Toast.makeText(this, type, Toast.LENGTH_LONG).show();
//			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("Read err");
		}
	}
	//�����ⷽ��
	 private static final boolean ping() {

		 String result = null;

		 try {

		 String ip = "www.baidu.com";// ���ǰٶȹ��ˣ����������Ӧ��û����~

		 Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);//ping3��


		 // ��ȡping�����ݣ��ɲ��ӡ�

		 InputStream input = p.getInputStream();

		 BufferedReader in = new BufferedReader(new InputStreamReader(input));

		 StringBuffer stringBuffer = new StringBuffer();

		 String content = "";

		 while ((content = in.readLine()) != null) {

		 stringBuffer.append(content);

		 }

		 Log.i("TTT", "result content : " + stringBuffer.toString());


		 // PING��״̬

		 int status = p.waitFor();

		 if (status == 0) {

		 result = "successful~";

		 return true;

		 } else {

		 result = "failed~ cannot reach the IP address";

		 }

		 } catch (IOException e) {

		 result = "failed~ IOException";

		 } catch (InterruptedException e) {

		 result = "failed~ InterruptedException";

		 } finally {

		 Log.i("TTT", "result = " + result);

		 }

		 return false;

	 }
    


}
