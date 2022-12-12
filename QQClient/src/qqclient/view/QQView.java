package qqclient.view;

import qqclient.service.FileClientService;
import qqclient.service.MessageClientService;
import qqclient.service.UserClientService;
import qqclient.utils.Utility;

/**
 * @author 李
 * @version 1.0
 */
public class QQView {
    private boolean loop = true;//控制是否显示菜单
    private String key = "";//用来接收用户的键盘输入
    private UserClientService userClientService = new UserClientService();//该对象用于登录服务/注册用户
    private MessageClientService messageClientService = new MessageClientService();//该对象用于私聊/群聊
    private FileClientService fileClientService = new FileClientService();//该对象用于文件传输
    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出系统......");
    }

    //显示主菜单
    public void mainMenu() {
        while (loop) {
            System.out.println("===========欢迎登陆网络通信系统===========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择：");
            key = Utility.readString(1);//读取键盘输入的指定长度的字符串

            //根据用户的输入，来处理不同的逻辑
            switch (key) {
                case "1":
                    System.out.print("请输入用户号：");
                    String userId = Utility.readString(50);//读取键盘输入的指定长度的字符串
                    System.out.print("请输入密  码：");
                    String pwd = Utility.readString(50);

                    // 到服务端去验证用户是否合法
                    //这里有很多代码,我们这里编写一个类UserClientService[提供用户登录/注册等功能]
                    if (userClientService.checkUser(userId, pwd)) {//验证成功
                        System.out.println("=========欢迎（用户 " + userId + " 登录成功）=========");
                        //进入到二级菜单
                        while (loop) {
                            System.out.println("\n=========网络通讯系统二级菜单（用户 " + userId + " ）==========");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    //这里写一个拉取用户在线列表的方法
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入想对大家说的话");
                                    String s = Utility.readString(100);
                                    //调用一个方法，将消息封装成 message对象，发给服务端
                                    messageClientService.sendMessageToAll(s,userId);
                                    break;
                                case "3":
                                    System.out.println("请输入想聊天的用户号（在线）：");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话：（100个字符以内）");
                                    String content = Utility.readString(100);
                                    //编写一个方法，将消息发送给服务端
                                    messageClientService.sendMessageToOne(content,userId,getterId);
                                    break;
                                case "4":
                                    System.out.println("请输入你希望发送文件的用户（在线）：");
                                    getterId = Utility.readString(50);
                                    System.out.println("请输入要发送文件的路径:(形式如：d:\\xx.jpg)");
                                    String src = Utility.readString(100);
                                    System.out.println("请输入把文件发送到对方电脑下的路径:(形式如：d:\\yy.jpg)");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src,dest,userId,getterId);
                                    break;
                                case "9":
                                    //调用方法，给服务器发送一个退出系统的message
                                    userClientService.logout();
                                    loop = false;//退出两层循环
                                    break;
                            }
                        }
                    } else {//验证失败
                        System.out.println("=========登录失败========");
                    }
                    break;
                case "9":
                    loop = false;//退出循环
                    break;
            }
        }
    }
}
