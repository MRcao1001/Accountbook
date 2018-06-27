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
	//获取界面输入
	String chosed;//选择的radiobutton内容
	String getStr;
	String userinfo[];
	//基于TCP协议网络程序的对象
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    //接收主界面传递来的userName
    String str;
    String str2;
    //用来dis服务器的dos
    String result;
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_account, container, false);
    }

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//获取userName
		final Bundle bundle = this.getArguments();
		button1 = (Button) getActivity().findViewById(R.id.bt_submit);
		et_addNoteName = (EditText)getActivity().findViewById(R.id.et_addNoteName);
		et_addMoney = (EditText)getActivity().findViewById(R.id.et_addMoney);
		rg_noteType = (RadioGroup)getActivity().findViewById(R.id.rg_noteType);
		rb_inside = (RadioButton)getActivity().findViewById(R.id.rb_inside);
		rb_out = (RadioButton)getActivity().findViewById(R.id.rb_out);
		tips = (TextView)getActivity().findViewById(R.id.tips);
		//获取username+homeNum
				if (bundle != null)
			    {
					str =bundle.getString("key");
					String userinfo[] = str.split("_");
					str2 = userinfo[0];
					
			    }
		
		//submit按钮,提交内容
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//界面内容写入username+homenum+_+...
				getStr = str+"_"+et_addNoteName.getText().toString()+"_"+et_addMoney.getText().toString()+"_"+chosed;
				if(str2.equals("请登录/注册")){
					tips.setText("请先登录在添加！");
					tips.setBackgroundColor(Color.parseColor("#bc364f"));
					return;
				}
				else{
					if(et_addNoteName.getText().toString().trim().isEmpty()){
						tips.setText("内容不能为空");
						tips.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(et_addMoney.getText().toString().trim().isEmpty()){
						tips.setText("金额不能为空");
						tips.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					addAccount();
					tips.setText("正在添加中，请稍候...");
					tips.setBackgroundColor(Color.parseColor("#35bd4b"));
					return;
					
				}
				
			}
		});
		//选择的值获取
		rg_noteType.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			  
            @Override  
            public void onCheckedChanged(RadioGroup group, int checkedId) {  
                // 获取选中的RadioButton的id  
                int id = group.getCheckedRadioButtonId();  
                // 通过id实例化选中的这个RadioButton  
                RadioButton choise = (RadioButton) getActivity().findViewById(id);  
                // 获取这个RadioButton的text内容  
                chosed = choise.getText().toString();  
                
            }  
        });
		
		
	}
	
	
	
	//添加数据函数，直接在onCreate中执行，用来添加数据到数据库
		public void addAccount() {
			// TODO Auto-generated method stub
			
	        //创建handle用于主线程和子线程通信
			final Handler mHandler = new Handler(){  
	    	    @Override  
	    	    public void handleMessage(Message msg) {  
	    	        super.handleMessage(msg);  
	    	        switch (msg.what) {  
	    	        //判断账号密码并返回主界面
	    	        case 0:
	    	        	String data = (String)msg.obj;
	    	        	if(data.equals("OK")){
	    	        		tips.setText("添加成功！");
	    					tips.setBackgroundColor(Color.parseColor("#3572bd"));
	    	        	}
	    	        	else
	    	        	{
	    	        		tips.setText("服务器异常");
	    					tips.setBackgroundColor(Color.parseColor("#bc364f"));
	    	        	}
	    	            break;  
	    	        default:  
	    	            break;  
	    	        }  
	    	    }  
	    	      
	    	};
	    	//中新建一个线程，创建socket嵌套字连接到4323
			new Thread() {
				   public void run(){

					   		//连接到4322
					   		try{
							
							socket = new Socket("193.112.137.246",4323);
								//193.112.137.246---172.18.126.68
							}
							catch(Exception e){
								System.out.print("socket err");
							}
							//创建输入输出流对象读取数据
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

							//读取数据
							
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
