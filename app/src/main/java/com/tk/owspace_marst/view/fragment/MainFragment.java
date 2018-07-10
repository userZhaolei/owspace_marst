package com.tk.owspace_marst.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tk.owspace_marst.R;
import com.tk.owspace_marst.app.GlideApp;
import com.tk.owspace_marst.model.entity.Item;
import com.tk.owspace_marst.view.activity.AudioDetailActivity;
import com.tk.owspace_marst.view.activity.DetailActivity;
import com.tk.owspace_marst.view.activity.VideoDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Zhaolei
 * 时间:2018/6/28
 */

public class MainFragment extends Fragment {

    String title;
    @BindView(R.id.image_iv)
    ImageView imageIv;
    @BindView(R.id.type_container)
    LinearLayout typeContainer;
    @BindView(R.id.comment_tv)
    TextView commentTv;
    @BindView(R.id.like_tv)
    TextView likeTv;
    @BindView(R.id.readcount_tv)
    TextView readcountTv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.author_tv)
    TextView authorTv;
    @BindView(R.id.type_tv)
    TextView typeTv;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.image_type)
    ImageView imageType;
    @BindView(R.id.download_start_white)
    ImageView downloadStartWhite;
    @BindView(R.id.home_advertise_iv)
    ImageView homeAdvertiseIv;
    @BindView(R.id.pager_content)
    RelativeLayout pagerContent;

    public static Fragment instance(Item item) {
        Fragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.item_main_page, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Item item = getArguments().getParcelable("item");
        final int model = Integer.valueOf(item.getModel()); //获取传输过来的bean
        if (model == 5) {   //除了5都是其他模式
            pagerContent.setVisibility(View.GONE);
            homeAdvertiseIv.setVisibility(View.VISIBLE);
            GlideApp.with(this.getContext()).load(item.getThumbnail()).centerCrop().into(homeAdvertiseIv);
        } else {
            pagerContent.setVisibility(View.VISIBLE);
            homeAdvertiseIv.setVisibility(View.GONE);
            title = item.getTitle();
            GlideApp.with(this.getContext()).load(item.getThumbnail()).centerCrop().into(imageIv);
            commentTv.setText(item.getComment());
            likeTv.setText(item.getGood());
            readcountTv.setText(item.getView());
            titleTv.setText(item.getTitle());
            contentTv.setText(item.getExcerpt());
            authorTv.setText(item.getAuthor());
            typeTv.setText(item.getCategory());
            switch (model) {
                case 2:
                    imageType.setVisibility(View.VISIBLE);
                    downloadStartWhite.setVisibility(View.GONE);
                    imageType.setImageResource(R.drawable.library_video_play_symbol);
                    break;
                case 3:
                    imageType.setVisibility(View.VISIBLE);
                    downloadStartWhite.setVisibility(View.VISIBLE);
                    imageType.setImageResource(R.drawable.library_voice_play_symbol);
                    break;
                default:
                    downloadStartWhite.setVisibility(View.GONE);
                    imageType.setVisibility(View.GONE);
            }
        }
        typeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (model) {
                    case 5:
                        Uri uri = Uri.parse(item.getHtml5());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), AudioDetailActivity.class); //音频
                        intent.putExtra("item", item);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), VideoDetailActivity.class);//视频
                        intent.putExtra("item", item);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), DetailActivity.class); //详情
                        intent.putExtra("item", item);
                        startActivity(intent);
                        break;
                    default:
                        intent = new Intent(getActivity(), DetailActivity.class);//详情
                        intent.putExtra("item", item);
                        startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
