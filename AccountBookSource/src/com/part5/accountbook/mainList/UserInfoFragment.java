package com.part5.accountbook.mainList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.part5.accountbook.MainActivity;
import com.part5.accountbook.R;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class UserInfoFragment extends Fragment {
	private LinearLayout ll_userName;
	private TextView tv_userName;
	private TextView tv_homeNum;
	String dataStream;
	//����TCPЭ���������Ķ���
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    //���������洫������userName
    String str;
    String str1;
    String str2;
    //����dis��������dos
    String result;
    //��������
    private Button button1;
    private ListView listView;
    private TextView tips1;
    
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_user_info, container,false);
		ll_userName = (LinearLayout) view.findViewById(R.id.ll_userName);
		tv_userName = (TextView) view.findViewById(R.id.tv_userName);
		tv_homeNum = (TextView) view.findViewById(R.id.tv_homeNum);
		ll_userName.setOnClickListener(new OnClickListener(){

			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent =  new Intent(getActivity(),MainActivity.class);
				startActivity(intent);
			}
			
		});
		Bundle bundle = this.getArguments();
		if (bundle != null)
	    {
			String str = bundle.getString("key");
			String userinfo[]=str.split("_");
			str1= userinfo[0];
			str2= userinfo[1];
	        tv_userName.setText(str1);
	        tv_homeNum.setText(str2);
	        
	    }
	    
		return view;
    }

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//��ȡuserName
		Bundle bundle = this.getArguments();
		listView = (ListView)getActivity().findViewById(R.id.list_userinfo);
		button1 = (Button) getActivity().findViewById(R.id.bt_userrefresh);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(str2.equals("���¼/ע��")){
					return;
				}
				else{
					Display();
					return;
					
				}
				
			}
		});
		if (bundle != null)
	    {
			str =bundle.getString("key");
			String userinfo[] = str.split("_");
			
	    }
	}
	//��ʾ���ݺ�����ֱ����onCreate��ִ�У�������ʾ���ݵ�����
		public void Display() {
			// TODO Auto-generated method stub
			
	        //����handle�������̺߳����߳�ͨ��
			final Handler mHandler = new Handler(){  
	    	    @Override  
	    	    public void handleMessage(Message msg) {  
	    	        super.handleMessage(msg);  
	    	        switch (msg.what) {  
	    	        //�ж��˺����벢����������
	    	        case 0:
	    	        	String data = (String)msg.obj;
	    	        	if(data != null){
	    	        		String notes[] = data.split("_");
	    	        		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, notes);    
	    	        		listView.setAdapter(adapter);
	    	        	}
	    	        	else
	    	        		button1.setText("error");
	    	            break;  
	    	        default:  
	    	            break;  
	    	        }  
	    	    }  
	    	      
	    	};
	    	//Display ���½�һ���̣߳�����socketǶ�������ӵ�4322
			new Thread() {
				   public void run(){

					   		//���ӵ�4322
					   		try{
							
							socket = new Socket("193.112.137.246",4330);
								//193.112.137.246---172.18.126.68
							}
							catch(Exception e){
								System.out.print("socket err");
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
								WriteString(str1);
								
							} catch (Exception e) {
								// TODO: handle exception
								System.out.print("socket clsoe err");
							}

							//��ȡ����
							
							try {
								Thread.sleep(2000);
								ReadStr();
								socket.close();
								Message message = new Message();
					            message.obj = result;
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
				result = dis.readUTF();
				dis.close();
				dos.close();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.print("Read err");
			}
		}

	
}
