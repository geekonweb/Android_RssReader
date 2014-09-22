//package com.lgq.rssreader.share;
//
//public class DoubanShare {
//	#region IBlogShare ��Ա
//
//    private string _apiKey;
//
//    public void BeginLogin(PhoneApplicationPage page, string apiKey, string secretKey)
//    {
//        _apiKey = apiKey;
//        
//        Helper.BuildLoginPage(page, AccountType.Douban);
//        
//        Helper.browserControl.Navigate(new Uri(
//            string.Format("https://www.douban.com/service/auth2/auth?client_id={0}&redirect_uri={1}&response_type=code&scope=shuo_basic_r,shuo_basic_w,community_basic_note",
//            apiKey,
//            "http://www.douban.com"), UriKind.Absolute));
//        Helper.browserControl.Navigated += (sender, eventArgs) =>
//        {
//            if (eventArgs.Uri.OriginalString.StartsWith("http://www.douban.com") && eventArgs.Uri.OriginalString.Contains("code"))
//            {                    
//                int index = eventArgs.Uri.OriginalString.IndexOf("?");
//                string[] values = eventArgs.Uri.OriginalString.Substring(index + 1).Split('&');
//                if(eventArgs.Uri.OriginalString.Contains("error"))
//                {
//                    string error = values.FirstOrDefault(v => v.Contains("error")).Split('=')[1];
//                    ShareCallBack(this, new ShareEventArgs<string>(false, error));
//                }
//                else
//                {
//                    string code = values.FirstOrDefault(v => v.Contains("code")).Split('=')[1];
//
//                    WebClient client = new WebClient();
//
//                    StringBuilder sb = new StringBuilder();
//                    
//                    client.Headers[HttpRequestHeader.ContentType] = "application/x-www-form-urlencoded";
//                    sb.Append("client_id=" + apiKey);
//                    sb.Append("&client_secret=" + secretKey);
//                    sb.Append("&redirect_uri=http://www.douban.com");
//                    sb.Append("&grant_type=authorization_code");
//                    sb.Append("&code=" + code);
//
//                    client.UploadStringAsync(new Uri("https://www.douban.com/service/auth2/token", UriKind.Absolute), "POST", HttpUtility.HtmlEncode(sb.ToString()));
//
//                    client.UploadStringCompleted += (o, args) =>
//                    {
//                        if (args.Error == null)
//                        {
//                            JObject result = JObject.Parse(args.Result);
//
//                            Deployment.Current.Dispatcher.BeginInvoke(() =>
//                            {
//                                Common.Helper.RemoveBrowser();
//                                if (LoginCallBack != null)
//                                {
//                                    LoginCallBack(this, new ShareEventArgs<string>(true, HttpUtility.UrlDecode(result["access_token"].Value<string>())));
//                                }
//                            });
//                        }
//                        else
//                        {
//                            ShareCallBack(this, new ShareEventArgs<string>(false, args.Error.Message));
//                        }
//                    };   
//                }                    
//            }
//        };                
//    }
//
//    public event LoginCompletedHandler<string> LoginCallBack;
//   
//    public void BeginShare(Blog blog, string accessToken)
//    {
//        WebClient client = new WebClient();
//
//        StringBuilder sb = new StringBuilder();
//        //title	�ռǱ���	�ش�������Ϊ��	
//        //privacy	��˽����	Ϊpublic��friend��private���ֲ���Ӧ���������ѿɼ������Լ��ɼ�
//        //can_reply	�Ƿ�����ظ�	�ش�, true����false
//        //content	�ռ�����, �����ͼƬ��ʹ�á�<ͼƬp_pid>��αtag����ͼƬ, ��������ӣ�ʹ��html�����ӱ�ǩ��ʽ������ֱ��ʹ����ַ	�ش�
//        client.Headers["Authorization"] = "Bearer " + accessToken;
//        sb.Append("source=" + _apiKey);
//        sb.Append("&rec_title=" + blog.Title);
//        sb.Append("&rec_url=" + blog.Link);
//        sb.Append("&text=" + Helper.parseHtml(blog.Description));            
//
//        client.Headers[HttpRequestHeader.ContentType] = "application/x-www-form-urlencoded; charset=UTF-8";
//        client.UploadStringAsync(new Uri("https://api.douban.com/shuo/v2/statuses/"), "POST", sb.ToString());
//
//        client.UploadStringCompleted += (sender, args) =>
//        {
//            if (args.Error == null)
//            {
//                if (ShareCallBack != null)
//                {
//                    ShareCallBack(this, new ShareEventArgs<string>(true, string.Empty));
//                }
//
//                //JObject obj = JObject.Parse(args.Result);
//
//                //if (obj["id"] == null || obj["id"].Value<int>() == 0)
//                //{
//                //    if (ShareCallBack != null)
//                //    {
//                //        if (obj["error_code"].Value<int>() == 2002) //accesstoken����                                                                
//                //            ShareCallBack(this, new ShareEventArgs<string>(false, Resources.StringResources.ReloginRenRen));
//                //        else
//                //            ShareCallBack(this, new ShareEventArgs<string>(false, obj["error_msg"].Value<string>()));
//                //    }
//                //}
//                //else
//                //{
//                //    if (ShareCallBack != null)
//                //    {
//                //        ShareCallBack(this, new ShareEventArgs<string>(true, string.Empty));
//                //    }
//                //}
//            }
//            else
//            {
//                if (LoginCallBack != null)
//                {
//                    LoginCallBack(this, new ShareEventArgs<string>(false, args.Error.Message));
//                }
//            }
//        };
//    }
//
//    public event ShareCompletedHandler<string> ShareCallBack;
//
//    #endregion
//}
