package yang.hong3.com.ryimnew.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import io.rong.imkit.fragment.ConversationFragment;
import yang.hong3.com.ryimnew.R;

/**
 * Created by hong3 on 2017-2-19.
 */

public class ConversationActivity extends FragmentActivity {

    ConversationFragment mFragment;
    FragmentManager mManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mManager = getSupportFragmentManager();
        mFragment = new ConversationFragment();
        mManager.beginTransaction().replace(R.id.rong_content,mFragment).commit();
    }


}
