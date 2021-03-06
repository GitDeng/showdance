package com.android.app.showdance.adapter;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.app.wumeiniang.R;
import com.android.app.showdance.logic.event.SharedEvent;
import com.android.app.showdance.logic.event.UploadDeleteEvent;
import com.android.app.showdance.logic.event.UploadEvent;
import com.android.app.showdance.model.UploadVideoInfo;
import com.android.app.showdance.model.glmodel.VideoUploadInfo.VideoUploadResponse;
import com.android.app.showdance.ui.PreSummeryEditorActivity;
import com.android.app.showdance.utils.ConstantsUtil;
import com.android.app.showdance.utils.FileUtil;
import com.android.app.showdance.utils.NetUtil;
import com.android.app.showdance.widget.CustomAlertDialog;
import com.android.app.showdance.widget.VideoRoundImageView;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import de.greenrobot.event.EventBus;

/**
 * 
 * @ClassName: RecordedVideoAdapter
 * @Description: 已录制视频
 * @author maminghua
 * @date 2015-5-14 下午03:51:19
 * 
 */
public class UploadedVideoAdapter extends BaseAdapter {

	private LayoutInflater listInflater;
	private Context context;

	private ListView recordedlv;

	private List<VideoUploadResponse> recordedVideoList;

	private MediaMetadataRetriever mediaMetadataRetriever;

	public UploadedVideoAdapter(Context context, List<VideoUploadResponse> recordedVideoList, ListView recordedlv) {
		this.context = context;
		this.listInflater = LayoutInflater.from(context);
		// 引用数据
		this.recordedVideoList = recordedVideoList;
		this.recordedlv = recordedlv;
	}

	public void notifyDataSetChanged(List<VideoUploadResponse>  list) {
		recordedVideoList = list;
		notifyDataSetChanged();
	}
	
	/**
	 * 获取数据集长
	 */
	@Override
	public int getCount() {
		return recordedVideoList.size();
	}

	/**
	 * 根据位置获取当前数据
	 */
	@Override
	public Object getItem(int position) {
		return recordedVideoList.get(position);
	}

	/**
	 * 获取位置
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}


	/**
	 * 代表某一个样式 的 数值(由position返回view type id)
	 */
	@Override
	public int getItemViewType(int position) {
		return position;
	}

	/**
	 * 返回你有多少个不同的布局样式
	 */
	@Override
	public int getViewTypeCount() {
		return 1;//recordedVideoList.size();
	}

	public final class ViewHolder {
		public ImageView a_img;
		public TextView music_name_tv; // 舞曲名
		public TextView music_singer_tv; // 歌曲演唱者
		public TextView music_size_tv; // 音乐大小
		public TextView download_count_tv; // 下载量
		public Button share;
		public ImageButton delete_btn;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			view = listInflater.inflate(R.layout.recordedvideo_item, parent, false);

			holder.a_img = (ImageView) view.findViewById(R.id.a_img);
			holder.music_singer_tv = (TextView) view.findViewById(R.id.music_singer_tv);
			holder.music_name_tv = (TextView) view.findViewById(R.id.music_name_tv);
			holder.music_size_tv = (TextView) view.findViewById(R.id.music_size_tv);
			holder.download_count_tv = (TextView) view.findViewById(R.id.download_count_tv);
			holder.share = (Button) view.findViewById(R.id.uploading_btn);
			holder.delete_btn = (ImageButton) view.findViewById(R.id.delete_btn);
//			holder.edit_btn = (Button) view.findViewById(R.id.edit_btn);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
//		((SwipeListView)parent).recycle(view, position);
		final VideoUploadResponse uploadVideo = recordedVideoList.get(position);
		String filename = uploadVideo.getpath().substring(uploadVideo.getpath().lastIndexOf("/")+1);
		String[] list = filename.split("_");
		holder.music_name_tv.setText(list[0]);
		if(list.length>=2)
			holder.music_singer_tv.setText(list[1]);
		if (list.length >= 3) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
				Date date = sdf.parse(list[2]);
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				holder.music_size_tv.setText(sdf.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		String bitmappath =uploadVideo.getimg();
			DisplayImageOptions opt = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheOnDisk(false)
			.build();
			ImageLoader.getInstance().displayImage(bitmappath, holder.a_img,opt);
//			mediaMetadataRetriever.release();
			holder.share.setVisibility(View.VISIBLE);
			holder.share.setText("分享");
			holder.share.setOnClickListener(new MyOnClick(holder, uploadVideo, position));
//			else if (videoType == 2) {
//			holder.edit_btn.setVisibility(View.VISIBLE);
//			holder.edit_btn.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//
//					Intent intent = new Intent(context,PreSummeryEditorActivity.class);
//					intent.putExtra("path", uploadVideo.getFilePath());
//					context.startActivity(intent);
//				}
//			});
//		}

//		if (uploadVideo.getUploadState() == 1) {
//			holder.uploading_btn.setText("已上传");
//			holder.uploading_btn.setTextColor(context.getResources().getColor(R.color.red));
//			holder.uploading_btn.setEnabled(false);
//		}

		// 点击"删除"
		holder.delete_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EventBus.getDefault().post(new UploadDeleteEvent(recordedVideoList.get(position).getid()));
			}
		});
		return view;
	}

	class MyOnClick implements OnClickListener {

		private int position;

		/**
		 * Title: Description:
		 */
		public MyOnClick(ViewHolder holder, VideoUploadResponse item, int position) {
			this.position = position;
		}

		/**
		 * (非 Javadoc) Title: onClick Description:
		 * 
		 * @param v
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			if (NetUtil.isWifiConnected(context)) {// 已开启wifi网络
				uploadVideoSelect(position);
			} else {// 未开启wifi网络
				new CustomAlertDialog(context).builder(R.style.DialogTVAnimWindowAnim).setTitle("网络提示").setMsg("WIFI网络未开启,是否继续使用2G或3G网络上传!").setPositiveButton("确  认", new OnClickListener() {
					@Override
					public void onClick(View v) {
						uploadVideoSelect(position);
					}
				}).setNegativeButton("取  消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
			}

		}
	}

	/**
	 * 上传选中的视频
	 * 
	 * @param position
	 * @param uploadMusicItem
	 * @param holder
	 */
	private void uploadVideoSelect(int position) {
		if (position <= recordedVideoList.size()) {
			VideoUploadResponse uploadMusicItem = recordedVideoList.get(position);
			EventBus.getDefault().post(new SharedEvent(uploadMusicItem));
		} else {
			Log.w("guolei","数组角标越界");
			// 数组角标越界
		}
	}

}
