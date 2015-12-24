package com.rongji.websiteMonitor.service.impl.message.vcm;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;



/**
 基于web或web service调用方式的基类
 */

public abstract class BaseWebService {

  private final static int TIMEOUT = 30 * 1000; //30秒,超时时间

  protected HttpClient hClient = new HttpClient();

  private String userName = ""; //用户名，外部设置
  private String password = ""; //口令，外部设置
  private String charset = "gbk"; //字符集

  protected BaseWebService() {
    hClient.setConnectionTimeout(TIMEOUT);
  }

  protected PostMethod getPostMethod(String URL) {
    PostMethod post = new PostMethod(URL);
    post.setFollowRedirects(false);
    return post;
  }

  protected GetMethod getGetMethod(String URL) {
    GetMethod get = new GetMethod(URL);
    return get;
  }

  public void setUserName(String userName) {
    if (userName == null) {
      this.userName = "";
    }
    else {
      this.userName = userName;
    }
  }

  public String getUserName() {
    return this.userName;
  }

  public void setPassword(String password) {
    if (password == null) {
      this.password = "";
    }
    else {
      this.password = password;
    }
  }

  public String getPassword() {
    return this.password;
  }

  public void setCharset(String charset) {
    if (charset == null) {
      this.charset = "gbk";
    }
    else {
      this.charset = charset;
    }
  }

  public String gettCharset() {
    return this.charset;
  }



  /** 使用http协议中的post方法
    URL:服务端http地址
    content: 传输的内容
    contentType： 内容类型 text/xml
    返回值：为服务端返回的内容
   */
  public String httpPost(String URL, String contentType, String content) {
    if (content == null || content.equals("")) {
      return null;
    }
    PostMethod post = getPostMethod(URL);
    post.setRequestHeader("Content-type", contentType + "; charset=" + charset);
    post.setRequestBody(content);
    try {
      hClient.executeMethod(post);
      return post.getResponseBodyAsString();
    }
    catch (Exception e) {
//      e.printStackTrace();
    }
    finally {
      post.releaseConnection();
    }
    return null;
  }

  /** 使用http协议中的get方法
    req: 请求参数和服务器地址
    返回值：为服务端返回的内容
   */
  public String httpGet(String req) {
    if (req == null || req.equals("")) {
      return null;
    }
    GetMethod post = getGetMethod(req);
    try {
      hClient.executeMethod(post);
      return post.getResponseBodyAsString();
    }
    catch (Exception e) {
      //
    }
    finally {
      post.releaseConnection();
    }
    return null;
  }

  /** 下面是使用Soap协议实现 **/
  /**
    使用soap协议
    host: 服务端请求地址
    soapAction:soap动作地址（http://tempuri.org/submit）
    values: 值对对象集合（内为ValuePairObject值对象）
    返回值：为服务端返回的内容
   */

}
