package com.szhua.androidhook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saurik.substrate.MS;

import java.lang.reflect.Method;

/**
 * Created by szhua on 2016/12/2.
 */

//hook的主要功能实现区域 ;
public class Main {


    /**
     * 静态的初始化方法 ;
     */
    static void initialize(){
        MS.hookClassLoad("com.tencent.mm.ui.LauncherUI", new MS.ClassLoadHook() {

            private Method getBaseContext;

            @Override
            public void classLoaded(final Class<?> classIndex) {


                // 获取BrowserActivity的onCreate方法
                Method onCreate;
                Method onCreateView ;
                Method[] methods ;
                Method[] methodss ;

                Method  addContentView ;
                Method attachBaseContext;
                try {
                    onCreate = classIndex.getMethod("onCreate", Bundle.class);
                } catch (Exception e) {
                    Log.e("test", "erro:no suchMethod onCreate") ;
                    onCreate =null ;
                }
                try {
                    getBaseContext=classIndex.getMethod("getBaseContext", null) ;
                } catch (Exception e) {
                    Log.e("test", "erro:no suchMethod getBaseContext") ;
                    getBaseContext =null ;
                }



                try {
                    addContentView =classIndex.getMethod("addContentView",View.class,ViewGroup.LayoutParams.class);
                } catch (Exception e) {
                    Log.e("test", "erro:no suchMethod addContentView") ;
                    addContentView =null ;
                }
                try {
                    attachBaseContext =classIndex.getMethod("attachBaseContext", Context.class) ;
                } catch (Exception e) {
                    Log.e("test", "erro:no suchMethod attachBaseContext") ;
                    attachBaseContext =null ;
                }

                try {
                    onCreateView =classIndex.getMethod("onCreateView", String.class,Context.class,AttributeSet.class) ;
                } catch (Exception e) {
                    Log.e("test", "erro:no suchMethod onCreateView") ;
                    onCreateView =null ;
                }

                //todo===============================================================
                /**
                 * Activity中的源码：
                 *
                 * ===onCreateView ;
                 public View onCreateView(String name, Context context, AttributeSet attrs) {
                 return super.onCreateView(name, context, attrs);
                 }

                 ==== onCreate ;
                 protected void onCreate(Bundle savedInstanceState) {
                 super.onCreate(savedInstanceState);

                 ==== addContentView ;
                 public void addContentView(View view, ViewGroup.LayoutParams params) {
                 view.getContext();
                 super.addContentView(view, params);
                 }

                 ====attachBaseContext
                 protected void attachBaseContext(Context newBase) {
                 super.attachBaseContext(newBase);
                 }

                 */


                if(onCreateView!=null){
                    final MS.MethodPointer old =new MS.MethodPointer() ;
                    // hook onCreate方法
                    MS.hookMethod(classIndex, onCreateView, new MS.MethodHook() {
                        public Object invoked(Object object, Object...args) throws Throwable {
                            Log.e("test", "onCreateView");
                            try {
                                Context  context= (Context) classIndex.newInstance() ;
                                Toast.makeText(context, "要弹出广告了", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e("test","here what is wrong:"+e.toString());
                            }
                            Object result =  old.invoke(object, args);

                            return result;
                        }
                    }, old);
                }

                if (onCreate != null) {
                    final MS.MethodPointer old = new MS.MethodPointer();
                    // hook onCreate方法
                    MS.hookMethod(classIndex, onCreate, new MS.MethodHook() {
                        public Object invoked(Object object, Object...args) throws Throwable {
                            Log.e("test", "show ad");
                            try {
                                Context  context= (Context) ((Method) getBaseContext).invoke(object, null) ;
                                Toast.makeText(context, "测试弹出广告位", Toast.LENGTH_SHORT).show() ;
                                Intent intent = new Intent();
                                ComponentName name = new ComponentName("com.szhua.androidhook"
                                        ,"com.szhua.androidhook.AdActivity");
                                intent.setComponent(name);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                                context.startActivity(intent);
                            } catch (Exception e) {
                                Log.e("test","here what is wrong:"+e.toString());
                            }
                            // 执行Hook前的onCreate方法，保证浏览器正常启动
                            Object result =  old.invoke(object, args);
                            return result;
                        }
                    }, old);
                }

                if(getBaseContext!=null){

                    final MS.MethodPointer old = new MS.MethodPointer();
                    // hook onCreate方法
                    MS.hookMethod(classIndex, getBaseContext, new MS.MethodHook() {
                        public Object invoked(Object object, Object...args) throws Throwable {
                            Log.e("test", "getBaseContext()");
                            // 执行Hook前的onCreate方法，保证浏览器正常启动
                            Context context =  (Context) old.invoke(object, args);
                            Toast.makeText(context, "福建省客服就开始", Toast.LENGTH_SHORT).show() ;
                            return context;
                        }
                    }, old);
                }

                if(attachBaseContext!=null){
                    final MS.MethodPointer old = new MS.MethodPointer();
                    // hook onCreate方法
                    MS.hookMethod(classIndex, attachBaseContext, new MS.MethodHook() {
                        public Object invoked(Object object, Object...args) throws Throwable {
                            Log.e("test", "attachBaseContext()");

                            Context context =(Context) args[0] ;
                            try {
                                Toast.makeText(context, "attachBaseContext（）", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.i("leilei", "this is why you are wrong !");
                            }
                            // 执行Hook前的onCreate方法，保证浏览器正常启动
                            Object result =   old.invoke(object, args);
                            return result;
                        }
                    }, old);
                }

                if(addContentView!=null){
                    final MS.MethodPointer old = new MS.MethodPointer();
                    // hook onCreate方法
                    MS.hookMethod(classIndex, addContentView, new MS.MethodHook() {
                        public Object invoked(Object object, Object...args) throws Throwable {
                            Log.e("test", "addContentView()");

                            View createdView =(View) args[0] ;
                            Context context =createdView.getContext() ;

                            try {
                                Toast.makeText(context, "attachBaseContext（）", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.i("leilei", "this is why you are wrong !");
                            }

                            // 执行Hook前的onCreate方法，保证浏览器正常启动
                            Object result =   old.invoke(object, args);

                            return result;
                        }
                    }, old);
                }

            }
        });



    }













}
