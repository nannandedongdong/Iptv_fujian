package com.ccdt.ottclient.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;

/**
 * 添加了监听器的Adapter
 * @author mark
 */
public abstract class BaseRecyclerViewAdapter extends Adapter<BaseRecyclerViewAdapter.BaseRecyclerViewHolder> {

	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}
	public interface OnItemFocusListener {
		public void onItemFocus(View view, boolean hasFocus, int position);
	}
	
	public interface OnItemLongClickListener {
		public void onItemLongClick(View view, int position);
	}
	
	private OnItemClickListener onItemClickListener;
	private OnItemLongClickListener onItemLongClickListener;
	private OnItemFocusListener onItemFocusListener;
	
	
	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		this.onItemClickListener = onItemClickListener;
	}
	
	
	public void setOnItemFocusListener(OnItemFocusListener onItemFocusListener){
		this.onItemFocusListener = onItemFocusListener;
	}
	
	public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
		this.onItemLongClickListener = onItemLongClickListener;
	}
	
	public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener,View.OnFocusChangeListener{

		public BaseRecyclerViewHolder(View view) {
			super(view);
			view.setOnClickListener(this);
			view.setOnLongClickListener(this);
			view.setOnFocusChangeListener(this);
			
		}

		@Override
		public boolean onLongClick(View v) {
			if (onItemLongClickListener != null){
				onItemLongClickListener.onItemLongClick(v, getAdapterPosition());
			}
			return true;
		}

		@Override
		public void onClick(View v) {
			if (onItemClickListener != null){
				onItemClickListener.onItemClick(v, getAdapterPosition());
			}
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (onItemFocusListener != null){
				onItemFocusListener.onItemFocus(v, hasFocus,getAdapterPosition());
			}
		}
		
	}

}
