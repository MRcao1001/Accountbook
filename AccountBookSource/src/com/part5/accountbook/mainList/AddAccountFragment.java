package com.part5.accountbook.mainList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.part5.accountbook.R;

import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class AddAccountFragment extends Fragment {
	private Button button1;
	private EditText et_addNoteName;
	private EditText et_addMoney;
	private RadioGroup rg_noteType;
	private RadioButton rb_inside;
	private RadioButton rb_out;
	private TextView tips;
	//��ȡ��������
	String chosed;//ѡ���radiobutton����
	String getStr;
	String userinfo[];
	//����TCPЭ���������Ķ���
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    //���������洫������userName
    String str;
    String str2;
    //����dis��������dos
    String result;
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_account, container, false);
    }

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//��ȡuserName
		final Bundle bundle = this.getArguments();
		button1 = (Button) getActivity().findViewById(R.id.bt_submit);
		et_addNoteName = (EditText)getActivity().findViewById(R.id.et_addNoteName);
		et_addMoney = (EditText)getActivity().findViewById(R.id.et_addMoney);
		rg_noteType = (RadioGroup)getActivity().findViewById(R.id.rg_noteType);
		rb_inside = (RadioButton)getActivity().findViewById(R.id.rb_inside);
		rb_out = (RadioButton)getActivity().findViewById(R.id.rb_out);
		tips = (TextView)getActivity().findViewById(R.id.tips);
		//��ȡusername+homeNum
				if (bundle != null)
			    {
					str =bundle.getString("key");
					String userinfo[] = str.split("_");
					str2 = userinfo[0];
					
			    }
		
		//submit��ť,�ύ����
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//��������д��username+homenum+_+...
				getStr = str+"_"+et_addNoteName.getText().toString()+"_"+et_addMoney.getText().toString()+"_"+chosed;
				if(str2.equals("���¼/ע��")){
					tips.setText("���ȵ�¼����ӣ�");
					tips.setBackgroundColor(Color.parseColor("#bc364f"));
					return;
				}
				else{
					if(et_addNoteName.getText().toString().trim().isEmpty()){
						tips.setText("���ݲ���Ϊ��");
						tips.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(et_addMoney.getText().toString().trim().isEmpty()){
						tips.setText("����Ϊ��");
						tips.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					addAccount();
					tips.setText("��������У����Ժ�...");
					tips.setBackgroundColor(Color.parseColor("#35bd4b"));
					return;
					
				}
				
			}
		});
		//ѡ���ֵ��ȡ
		rg_noteType.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			  
            @Override  
            public void onCheckedChanged(RadioGroup group, int checkedId) {  
                // ��ȡѡ�е�RadioButton��id  
                int id = group.getCheckedRadioButtonId();  
                // ͨ��idʵ����ѡ�е����RadioButton  
                RadioButton choise = (RadioButton) getActivity().findViewById(id);  
                // ��ȡ���RadioButton��text����  
                chosed = choise.getText().toString();  
                
            }  
        });
		
		
	}
	
	
	
	//������ݺ�����ֱ����onCreate��ִ�У�����������ݵ����ݿ�
		public void addAccount() {
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
	    	        	if(data.equals("OK")){
	    	        		tips.setText("��ӳɹ���");
	    					tips.setBackgroundColor(Color.parseColor("#3572bd"));
	    	        	}
	    	        	else
	    	        	{
	    	        		tips.setText("�������쳣");
	    					tips.setBackgroundColor(Color.parseColor("#bc364f"));
	    	        	}
	    	            break;  
	    	        default:  
	    	            break;  
	    	        }  
	    	    }  
	    	      
	    	};
	    	//���½�һ���̣߳�����socketǶ�������ӵ�4323
			new Thread() {
				   public void run(){

					   		//���ӵ�4322
					   		try{
							
							socket = new Socket("193.112.137.246",4323);
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
								//Thread.sleep(1000);
								WriteString(getStr);
								
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
