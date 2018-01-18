package com.seckawijoki.graduation_project.functions.group_editor;
/**
 * Created by 瑶琴频曲羽衣魂 on 2017/12/10 at 15:43.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.seckawijoki.graduation_project.R;
import com.seckawijoki.graduation_project.db.server.FavoriteGroupType;

import java.util.ArrayList;
import java.util.List;

class GroupEditorAdapter extends RecyclerView.Adapter<GroupEditorAdapter.ViewHolder> {
  private static final String TAG = "GroupEditorAdapter";
  private Context context;
  private List<FavoriteGroupType> favoriteGroupTypeList;
  private List<Boolean> selectedList;
  private OnGroupEditorClickListener listener;
  GroupEditorAdapter(Context context) {
    this.context = context;
  }
  List<String> getSelectedGroup(){
    List<String> selectedGroupList = new ArrayList<>();
    for ( int i = 0 ; i < selectedList.size() ; i++ ) {
      Boolean aBoolean = selectedList.get(i);
      if (aBoolean){
        selectedGroupList.add(favoriteGroupTypeList.get(i).getFavoriteGroupName());
      }
    }
    return selectedGroupList;
  }
  public GroupEditorAdapter setFavoriteGroupTypeList(List<FavoriteGroupType> groupNameList) {
    this.favoriteGroupTypeList = groupNameList;
    if (selectedList == null || selectedList.size() !=  this.favoriteGroupTypeList.size() || selectedList.size() == 0 ){
      selectedList = new ArrayList<>();
      for ( int i = 0 ; i < groupNameList.size() ; i++ ) {
        selectedList.add(false);
      }
    }
    return this;
  }
  public GroupEditorAdapter setOnGroupEditorClickListener(OnGroupEditorClickListener listener) {
    this.listener = listener;
    return this;
  }
  private boolean checkSelected(){
    for ( int i = 0 ; i < selectedList.size() ; i++ ) {
      if (selectedList.get(i))return true;
    }
    return false;
  }
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.list_item_group_editor, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Log.i(TAG, "onBindViewHolder(): ");
    if ( favoriteGroupTypeList != null && position == favoriteGroupTypeList.size()){
      float fontSize =
              context.getResources().getDimension(R.dimen.ts_tip_click_to_rename) /
                      context.getResources().getDisplayMetrics().density;
      holder.chb.setVisibility(View.INVISIBLE);
      holder.imgSetTop.setVisibility(View.GONE);
      holder.imgReorder.setVisibility(View.INVISIBLE);
      holder.tv.setText(context.getString(R.string.action_click_to_rename_group));
      holder.tv.setBackgroundColor(Color.TRANSPARENT);
      holder.tv.setGravity(Gravity.CENTER);
      holder.tv.setTextSize(fontSize);
    } else if ( favoriteGroupTypeList != null && position < favoriteGroupTypeList.size()) {
      if (position == 0){
        holder.imgSetTop.setVisibility(View.GONE);
      }
      FavoriteGroupType favoriteGroupType = favoriteGroupTypeList.get(position);
      String groupName = favoriteGroupType.getFavoriteGroupName();
      holder.tv.setText(String.format(
              context.getString(R.string.format_group_name),
              groupName, favoriteGroupType.getStockCount()
      ));
      if ( favoriteGroupType.isEditable()) {
        holder.tv.setOnClickListener(v -> {
          listener.onGroupRename(favoriteGroupTypeList.get(position).getFavoriteGroupName());
        });
        holder.chb.setOnClickListener(v -> {
          boolean selected = selectedList.get(position);
          selectedList.set(position, !selected);
          listener.onGroupChoose(checkSelected());
          Log.d(TAG, "onBindViewHolder(): selectedList = " + selectedList);
        });
      } else {
        holder.chb.setVisibility(View.INVISIBLE);
      }
    }
  }

  @Override
  public int getItemCount() {
    return favoriteGroupTypeList == null ? 0 : favoriteGroupTypeList.size() + 1;
  }

  static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    CheckBox chb;
    TextView tv;
    ImageView imgSetTop;
    ImageView imgReorder;
    ViewHolder(View view) {
      super(view);
      chb = view.findViewById(R.id.chb_choose_to_delete);
      tv = view.findViewById(R.id.tv_group_name);
      imgSetTop = view.findViewById(R.id.img_set_top);
      imgReorder = view.findViewById(R.id.img_reorder);
    }

    @Override
    public void onClick(View v) {
    }
  }
}