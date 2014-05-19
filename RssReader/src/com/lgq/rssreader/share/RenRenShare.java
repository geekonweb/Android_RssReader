//package com.lgq.rssreader.share;
//
//public class RenRenShare {
//	 #region IBlogShare ��Ա
//
//     private string _secretKey;
//
//     public void BeginLogin(PhoneApplicationPage page, string apiKey, string secretKey)
//     {
//         _secretKey = secretKey;
//	        RenRenAPI api = new RenRenAPI(apiKey, secretKey);
//         Helper.BuildLoginPage(page,AccountType.RenRen);
//         List<string> scope = new List<string> { "publish_blog" };
//         api.Login(page, scope, (sender, eventArgs) =>
//                                     {
//                                         if(eventArgs.Result)
//                                         {
//                                             RenRenSDKData data = (RenRenSDKData) sender;
//                                             Deployment.Current.Dispatcher.BeginInvoke(() =>
//                                             {
//                                                 Common.Helper.RemoveBrowser();
//                                                 if (LoginCallBack != null)
//                                                 {
//                                                     LoginCallBack(this, new ShareEventArgs<string>(true, HttpUtility.UrlDecode(data.AccessToken)));
//                                                 }   
//                                             });                                                 
//                                         }
//                                         else
//                                         {
//                                             if (LoginCallBack != null)
//                                             {
//                                                 LoginCallBack(this, new ShareEventArgs<string>(false, eventArgs.Data));
//                                             } 
//                                         }
//                                     });
//     }
//
//     public event LoginCompletedHandler<string> LoginCallBack;
//
//     private string SignRenRen(IOrderedEnumerable<KeyValuePair<string, string>> param, string secretKey)
//     {
//         StringBuilder sb = new StringBuilder();
//         foreach (KeyValuePair<string, string> pair in param)
//         {
//             sb.Append(pair.Key + "=" + pair.Value);
//         }
//         sb.Append(secretKey);
//
//         return MD5.GetMd5String(sb.ToString());
//     }
//
//     public void  BeginShare(Blog blog, string accessToken)
//     {
//	        IDictionary<string, string> param = new Dictionary<string, string>();
//
//         //Required	Name	Type	Description
//         //required	sig	string	ǩ����֤�����õ�����������в������������ֵ������˴��鿴��ϸ�㷨
//         //method	string	blog.addBlog
//         //v	string	API�İ汾�ţ��̶�ֵΪ1.0
//         //title	string	��־�ı���
//         //content	string	��־������
//         //alternative	api_key	string	����Ӧ��ʱ�����api_key�����ýӿ�ʱ�����Ӧ�õ�Ψһ���
//         //session_key	string	��ǰ�û���session_key
//         //access_token	string	OAuth2.0��֤��Ȩ���õ�token��������˲���ʱ��api_key��session_key���Բ��ô��롣
//         //optional	format	string	����ֵ�ĸ�ʽ����ָ��ΪJSON����XML���Ƽ�ʹ��JSON��ȱʡֵΪXML
//         //visable	int	��־����˽���ã�����ֵ��99(�����˿ɼ�)1(�����ѿɼ�)4(��Ҫ����)-1(���Լ��ɼ�),����û��,Ĭ��Ϊ99
//         //password	string	�û����õ�����
//
//         param.Add("method", "blog.addBlog");
//         param.Add("v","1.0");
//         param.Add("title", blog.Title);
//         param.Add("content", string.IsNullOrEmpty(blog.Content) ? blog.Description : blog.Content);
//         param.Add("url", blog.Link);            
//         param.Add("format", "JSON");
//         param.Add("access_token", accessToken);            
//
//         param.Add("sig", SignRenRen(param.OrderBy(p => p.Key),_secretKey));
//
//         WebClient client = new WebClient();
//
//         StringBuilder sb = new StringBuilder();
//
//         foreach (KeyValuePair<string, string> pair in param)
//         {
//             sb.Append(pair.Key + "=" + HttpUtility.UrlEncode(pair.Value) + "&");
//         }
//
//         sb.Remove(sb.Length - 1, 1);
//
//         client.Headers[HttpRequestHeader.ContentType] = "application/x-www-form-urlencoded; charset=UTF-8";
//         client.UploadStringAsync(new Uri("http://api.renren.com/restserver.do"), "POST", sb.ToString());            
//
//         client.UploadStringCompleted += (sender, args) =>
//         {
//             if(args.Error == null)
//             {
//                 JObject obj = JObject.Parse(args.Result);
//
//                 if (obj["id"] == null || obj["id"].Value<int>() == 0)
//                 {
//                     if(ShareCallBack != null)
//                     {   
//                         if(obj["error_code"].Value<int>() == 2002) //accesstoken����                                                                
//                             ShareCallBack(this, new ShareEventArgs<string>(false, Resources.StringResources.ReloginRenRen));                                                                
//                         else
//                             ShareCallBack(this, new ShareEventArgs<string>(false, obj["error_msg"].Value<string>()));
//                     }
//                 }
//                 else
//                 {
//                     if(ShareCallBack != null)
//                     {
//                         ShareCallBack(this, new ShareEventArgs<string>(true, string.Empty));
//                     }
//                 }
//             }
//             else
//             {
//                 if (LoginCallBack != null)
//                 {
//                     LoginCallBack(this, new ShareEventArgs<string>(false, args.Error.Message));
//                 }
//             }
//         };
//     }
//
//     public event ShareCompletedHandler<string> ShareCallBack;
//
//     #endregion
//}
