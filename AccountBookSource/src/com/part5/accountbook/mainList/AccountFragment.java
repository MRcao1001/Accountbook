package com.part5.accountbook.mainList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.part5.accountbook.MainActivity;
import com.part5.accountbook.R;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class AccountFragment extends Fragment {
	//����TCPЭ���������Ķ���
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    //���������洫������userName
    String str;
    String str2;
    //����dis��������dos
    String result;
    //��������
    private Button button1;
    private ListView listView;
    private TextView tips1;
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_account_list, container, false);
        
        
        
    }
	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//��ȡuserName
		Bundle bundle = this.getArguments();
		listView = (ListView)getActivity().findViewById(R.id.list_view);
		tips1 = (TextView)getActivity().findViewById(R.id.tips1);
		button1 = (Button) getActivity().findViewById(R.id.bt_refresh);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(str2.equals("���¼/ע��")){
					tips1.setText("���ȵ�¼�ڻ�ȡ���ݣ�");
					tips1.setBackgroundColor(Color.parseColor("#bc364f"));
					return;
				}
				else{
					Display();
					tips1.setText("���ݻ�ȡ��...");
					tips1.setBackgroundColor(Color.parseColor("#35bd4b"));
					return;
					
				}
				
			}
		});
		if (bundle != null)
	    {
			str =bundle.getString("key");
			String userinfo[] = str.split("_");
			str2 = userinfo[0];
			
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
	    	        		tips1.setText("��ȡ�ɹ�");
	    					tips1.setBackgroundColor(Color.parseColor("#3572bd"));
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
							
							socket = new Socket("193.112.137.246",4322);
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
								WriteString(str);
								
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
