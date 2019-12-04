package mx.rfpro.uhfreader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mx.rfpro.uhfreader.R;

public class TagAdpater extends RecyclerView.Adapter<TagAdpater.TagViewHolder>{


    List<String> tagList;


    public TagAdpater() {
        this.tagList = new ArrayList<String>();
    }

    public void updateList(Set<String> tagSet) {
        tagList.clear();
        this.tagList.addAll(tagSet);

    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_layout, parent, false);

        TagViewHolder tagViewHolder = new TagViewHolder(v);
        return tagViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {

        String tag = tagList.get(position);
        holder.textViewTag.setText(tag);


    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTag;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTag = itemView.findViewById(R.id.textViewTag);
        }


    }
}
