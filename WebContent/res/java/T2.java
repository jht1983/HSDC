package yulongtao.test;
import com.yulongtao.db.*;
import javax.servlet.http.HttpServletRequest;

public class T2{
	public static String PATH =  "D:\\OAXiTongXuQiuFenXi20111019.doc";  
    public static String OUTPATH = PATH + ".txt";  
    public static String getMethod(){
       return "method exe cucces!";
    }
    public static String get5(){
        //0所有按钮
        //-1是不现时复制
        //-2不显示查询
        //doFlow();
        //淇敼椤甸潰澶у皬鎸夋柊澧為〉闈㈠ぇ灏忔潵锛屽鏋滄病鏈夋柊澧為〉闈紶鍏ュ弬鏁皊ys_edit_size=瀹?,楂樺鏋滈兘娌℃湁浣跨敤榛樿
        //NO_VIEW_FLOW_BTTN=yes //闅愯棌鍙戣捣娴佺▼鎸夐挳
        //charset
        //sys_auto_key
        //NO_QDBTTNNO_QXBTTN sys_op_del_bttn=a
        //strISCOLTOROW鍒楃紪琛?
        //com.yulongtao.util SerialUtil  public static String getSerialNum(String _strCodeId,HttpServletRequest _request)
        //TAB  SPLIT PAG
        //NO_sys_docommand
        //fun_
        //NO_QDBTTN=no
        //sys_flow_run_id杩愯
        /**
         * <org.xwalk.core.XWalkView android:id="@+id/activity_main"
        xmlns:android="http://schemas.android.com/apk/res/android"
        **/
        return PATH+"=========================";
    }
    
     public static String getUserInfo(String str){
        String strVresult="";
        try{
            TableEx tableEx=new TableEx("*","T_RGXX","");
            int iRecordC=tableEx.getRecordCount();
            Record record;
            for(int i=0;i<iRecordC;i++){
                record=tableEx.getRecord(i);
                strVresult+=record.getFieldByName("SYGMC").value+",";
            }
        }catch(Exception e){
            strVresult=e.toString();
        }
        return str;
    }
     public static String get10(){
        return PATH+"===this is 8";
    }
     public static String a(){
        return PATH+"===3a";
    }
     public static String b(){
        return PATH+"===b";
    }
     public static String c(){
        return PATH+"===a";
    }
    public static String go12(){
        String strVresult="";
        try{
            TableEx tableEx=new TableEx("*","T_RGXX","");
            int iRecordC=tableEx.getRecordCount();
            Record record;
            for(int i=0;i<iRecordC;i++){
                record=tableEx.getRecord(i);
                strVresult+=record.getFieldByName("SYGMC").value+",";
            }
        }catch(Exception e){
            strVresult=e.toString();
        }
        return strVresult;
    }


}
