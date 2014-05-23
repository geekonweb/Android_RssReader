package com.lgq.rssreader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.lgq.rssreader.dal.SyncStateDalHelper;
import com.lgq.rssreader.entity.Blog;
import com.lgq.rssreader.entity.Channel;
import com.lgq.rssreader.entity.SyncState;
import com.lgq.rssreader.enums.RssAction;
import com.lgq.rssreader.parser.FeedlyParser;
import com.lgq.rssreader.parser.HttpResponseHandler;
import com.lgq.rssreader.utils.Helper;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * An activity representing a single Blog detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MainActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link BlogContentFragment}.
 */
public class BlogListActivity extends FragmentActivity implements BlogListFragment.Callbacks  {
	
	private boolean needUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_blog_list);

        // Show the Up button in the action bar.
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
        	
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = getIntent().getExtras();
            
            if(arguments.containsKey(BlogListFragment.ARG_ITEM_ID)){
            	Channel c = (Channel)arguments.get(BlogListFragment.ARG_ITEM_ID);                
                
                BlogListFragment fragment = new BlogListFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.blog_list_container, fragment)
                        .commit();
            }            
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			Intent i = new Intent();
			i.putExtra("NeedUpdate", needUpdate);
			this.setResult(Activity.RESULT_OK, i);
						
		}
		
		return super.onKeyDown(keyCode, event);
    }

	@Override
	public void onItemSelected(Blog p, Blog c, Blog n) {
		// TODO Auto-generated method stub
		Intent detailIntent = new Intent(this, BlogContentActivity.class);
        Bundle arguments = new Bundle();        
        if(c != null)
        	arguments.putSerializable(BlogContentFragment.CURRENT, c);        
        arguments.putSerializable(BlogContentFragment.CHANNEL, (Channel)getIntent().getExtras().get(BlogListFragment.ARG_ITEM_ID));
        detailIntent.putExtras(arguments);
        startActivityForResult(detailIntent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (resultCode)
		{
			case RESULT_OK:
				String gson = data.getStringExtra("Blogs");
				Gson g = new Gson();
				
				final List<Blog> blogs = new ArrayList<Blog>();
				final List<String> blogIDs = new ArrayList<String>();
				
				for(String s : gson.split("____")){
					if(s != null && s.length() > 0){
						Blog b = g.fromJson(s, Blog.class);
						blogs.add(b);
						blogIDs.add(b.BlogId);
					}
				}
				
				final FeedlyParser feedly = new FeedlyParser();
				
				int count = data.getIntExtra("Count", 0);
	            
	            if(blogs.size() > 0){
	            	final Blog b = blogs.get(0);
	            	
//	            	feedly.getCount(b.ChannelId, new HttpResponseHandler(){
//		            	@Override
//		            	public <Integer> void onCallback(Integer count, boolean result, String msg){
//		            		if(result){
//		            			
//		            			BlogListFragment fragment =(BlogListFragment)getSupportFragmentManager(). findFragmentById(R.id.blog_list_container);
//								
//		            			if(fragment != null){
//		            				Message m = fragment.myHandler.obtainMessage();
//			        	            m.what = 3;
//			        	            m.obj = count;
//			        	            fragment.myHandler.sendMessage(m);
//			        	            
//			        	            needUpdate = true;
//			        	            			        	            
//			        	            Helper.updateChannels(b.ChannelId, (java.lang.Integer) count);
//		            			}
//		            		}
//		            	}
//		            });
	            	
	            	BlogListFragment fragment =(BlogListFragment)getSupportFragmentManager(). findFragmentById(R.id.blog_list_container);
					
	            	Channel target = Helper.findChannelById(Helper.getChannels(), b.ChannelId);
	            	
        			if(fragment != null){
        				Message m = fragment.myHandler.obtainMessage();
        	            m.what = BlogListFragment.UPDATECOUNT;
        	            m.obj = target.UnreadCount - count > 0 ? target.UnreadCount - count : 0 ;//blogs.size();
        	            fragment.myHandler.sendMessage(m);
        	            
        	            needUpdate = true;
        	            			        	            
        	            Helper.updateChannels(b.ChannelId, target.UnreadCount - blogs.size());
        			}
	            }
				
				BlogListFragment fragment =(BlogListFragment)getSupportFragmentManager(). findFragmentById(R.id.blog_list_container);
								
				Message m = fragment.myHandler.obtainMessage();
	            m.what = BlogListFragment.UPDATESTATE;
	            m.obj = blogs;
	            fragment.myHandler.sendMessage(m);
				break;
			default:
				break;
		}
	}
}
