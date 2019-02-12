package carga.tcss450.uw.edu.phishapp;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import carga.tcss450.uw.edu.phishapp.BlogFragment.OnBlogListFragmentInteractionListener;
import carga.tcss450.uw.edu.phishapp.blog.BlogPost;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BlogPost } and makes a call to the
 * specified {@link OnBlogListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBlogRecyclerViewAdapter extends RecyclerView.Adapter<MyBlogRecyclerViewAdapter.ViewHolder> {

    private final List<BlogPost> mValues;
    private final BlogFragment.OnBlogListFragmentInteractionListener mListener;

    public MyBlogRecyclerViewAdapter(List<BlogPost > items, BlogFragment.OnBlogListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_blog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mPubDateView.setText(mValues.get(position).getPubDate());
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mTeaserView.setText(Html.fromHtml(mValues.get(position).getTeaser(),
                                    Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE,
                                    null,
                                    null));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onBlogListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPubDateView;
        public final TextView mTitleView;
        public final TextView mTeaserView;
        public BlogPost mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPubDateView = (TextView) view.findViewById(R.id.textview_fragment_blog_publish_date);
            mTitleView = (TextView) view.findViewById(R.id.textview_fragment_blog_blog_title);
            mTeaserView = (TextView) view.findViewById(R.id.textview_fragment_blog_blog_teaser);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
