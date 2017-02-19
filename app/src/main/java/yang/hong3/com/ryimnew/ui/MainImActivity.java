package yang.hong3.com.ryimnew.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation;
import yang.hong3.com.ryimnew.R;
import yang.hong3.com.ryimnew.adapter.ConversationListAdapterEx;
import yang.hong3.com.ryimnew.controller.MainImController;
import yang.hong3.com.ryimnew.ui.view.MainImView;
import yang.hong3.com.ryimnew.util.NLog;

public class MainImActivity extends BaseActivity implements MainImView {

    private static final String TAG = "MainImActivity";
    public static final int ADD_FRIEND = 3;
    public static final int TO_CHAT = 5;


    EditText userId;
    String targetId;
    MainImController mController;

    //会话的聊天窗口
    ConversationListFragment mListFragment;
    FragmentManager mManager;


    public static int status = -1;
    private boolean isDebug=true;
    private Conversation.ConversationType[] mConversationsTypes;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main_im;
    }

    @Override
    protected void initView() {
        userId = $(R.id.user_id);

        mListFragment = (ConversationListFragment) initConversationList();
        mManager = getSupportFragmentManager();
        mManager.beginTransaction().replace(R.id.chat_content,mListFragment).commit();

        mController = new MainImController(this);


    }

    private void click() {
        //实现会话列表操作监听
        RongIM.setConversationListBehaviorListener(new RongIM.ConversationListBehaviorListener() {
            /**
             * 当点击会话头像后执行。
             *
             * @param context          上下文。
             * @param conversationType 会话类型。
             * @param targetId         被点击的用户id。
             * @return 如果用户自己处理了点击后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
             */
            @Override
            public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String targetId) {
                NLog.d(TAG, "onConversationPortraitClick: "+conversationType.getName()+"  -->"+targetId);
                return true;
            }

            /**
             * 当长按会话头像后执行。
             *
             * @param context          上下文。
             * @param conversationType 会话类型。
             * @param targetId         被点击的用户id。
             * @return 如果用户自己处理了点击后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
             */

            @Override
            public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String targetId) {
                NLog.d(TAG,"onConversationPortraitLongClick:  "+conversationType.getName()+"   -->"+targetId);
                return true;
            }

            /**
             * 长按会话列表中的 item 时执行。
             *
             * @param context        上下文。
             * @param view           触发点击的 View。
             * @param uiConversation 长按时的会话条目。
             * @return 如果用户自己处理了长按会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
             */

            @Override
            public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
                NLog.d(TAG,"onConversationLongClick:  "+uiConversation.getConversationSenderId()+"  ->"+uiConversation.getConversationTargetId()+
                " -->"+uiConversation.getIconUrl());

                return true;
            }

            /**
             * 点击会话列表中的 item 时执行。
             *
             * @param context        上下文。
             * @param view           触发点击的 View。
             * @param uiConversation 会话条目。
             * @return 如果用户自己处理了点击会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
             */
            @Override
            public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
                NLog.d(TAG,"onConversationClick:  "+uiConversation.getConversationSenderId()+"  ->"+uiConversation.getConversationTargetId()+
                        " -->"+uiConversation.getIconUrl());
                return true;
            }
        });
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.add_friend:
                targetId = userId.getText().toString();
                status = ADD_FRIEND;
                if (!TextUtils.isEmpty(targetId) && status != -1) {
                    mController.queryFriend(targetId);
                }
                break;
            case R.id.goto_chat:
                targetId = userId.getText().toString();
                status = TO_CHAT;
                if (!TextUtils.isEmpty(targetId) && status != -1) {
                    mController.queryFriend(targetId);
                }
                break;
        }
    }

    private Fragment initConversationList() {
        if (mListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM
                };
            }
            listFragment.setUri(uri);
            mListFragment = listFragment;
            return listFragment;
        } else {
            return mListFragment;
        }
    }

    public void showPop(final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (status == ADD_FRIEND) {
            builder.setTitle("找到好友")
                    .setMessage(str)
                    .setPositiveButton("添加好友", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            fastShow("添加好友");

                            dialog.dismiss();
                        }
                    });

        } else if (status == TO_CHAT) {
            builder.setTitle("找到好友")
                    .setMessage(str)
                    .setPositiveButton("去聊天", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fastShow("去聊天");
                            if (RongIM.getInstance() != null){
                                RongIM.getInstance().startPrivateChat(MainImActivity.this,targetId,str);
                            }
                            dialog.dismiss();
                        }
                    });
        }

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fastShow("取消");
                dialog.dismiss();
            }
        })
                .create()
                .show();
    }


    @Override
    public void quryFriendSuccess(String userName) {
        showPop(userName);
    }

    @Override
    public void fails(String str) {
        fastShow(str);
    }


}
