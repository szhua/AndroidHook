# AndroidHook

---

Android hook used saurik.substrate;insert something into ohter process ;this project insert into wechat!!
网上查了很多的材料，关于saurik.substrate这个框架的东西不多，自己写了这么一个demo，希望能够帮助想对这方面有了解的人。共同交流！


 - 通过adb命令我们可以得到当前手机运行的activity，这里我抓取了微信的登录界面com.tencent.mm.ui.LauncherUI  ;
 - 通过反射机制获得LauncherUI界面的方法：这里主要抽取onCreate,getBaseContext两个方法；
 - 然后使用hook技术，在某个方法执行时执行我们自定义的方法；

```java
static void initialize(){

       
        MS.hookClassLoad("com.tencent.mm.ui.LauncherUI", new MS.ClassLoadHook() {

            /**
            通过这个方法我们获得Context对象；
            */
            private Method getBaseContext;

            @Override
            public void classLoaded(final Class<?> classIndex) {


                // 获取BrowserActivity的onCreate方法
                Method onCreate;
                Method onCreateView ;
                
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



              
            
                if (onCreate != null&&getBaseContext!=null) {
                    final MS.MethodPointer old = new MS.MethodPointer();
                    // hook onCreate方法
                    MS.hookMethod(classIndex, onCreate, new MS.MethodHook() {
                        public Object invoked(Object object, Object...args) throws Throwable {
                            Log.e("test", "show ad");
                            try {
                            //使用Method.invoke方法执行规定的方法 ；
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
              Toast.makeText(context,"执行了getBaseContext方法！！",Toast.LENGTH_SHORT).show() ;
                            return context;
                        }
                    }, old);
                }

               
        });



    }
```


