package org.ladlb.directassemblee.timeline

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_timeline_pager.*
import kotlinx.android.synthetic.main.item_deputy.*
import org.ladlb.directassemblee.AbstractToolBarActivity
import org.ladlb.directassemblee.R
import org.ladlb.directassemblee.data.CacheManager
import org.ladlb.directassemblee.deputy.Deputy
import org.ladlb.directassemblee.deputy.DeputyHelper
import javax.inject.Inject

/**
 * This file is part of DirectAssemblee-Android <https://github.com/direct-assemblee/DirectAssemblee-Android>.
 *
 * DirectAssemblee-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DirectAssemblee-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DirectAssemblee-Android. If not, see <http://www.gnu.org/licenses/>.
 */

class TimelinePagerActivity : AbstractToolBarActivity(), OnPageChangeListener {

    companion object Factory {

        var EXTRA_DEPUTY: String = "EXTRA_DEPUTY"

        var EXTRA_POSITION: String = "EXTRA_POSITION"

        fun getIntent(context: Context, cacheManager: CacheManager, deputy: Deputy, position: Int, items: ArrayList<TimelineItem>): Intent {

            val intent = Intent(context, TimelinePagerActivity::class.java)
            intent.putExtra(EXTRA_DEPUTY, deputy)
            intent.putExtra(EXTRA_POSITION, position)

            cacheManager.put(
                    CacheManager.timeLine,
                    items
            )

            return intent

        }

    }

    @Inject
    lateinit var cacheManager: CacheManager

    private lateinit var adapter: TimelinePagerAdapter

    override fun getContentView(): Int = R.layout.activity_timeline_pager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras!!
        val position = bundle.getInt(EXTRA_POSITION, 0)
        val deputy = bundle.getParcelable<Deputy>(EXTRA_DEPUTY)!!

        @Suppress("UNCHECKED_CAST")
        adapter = TimelinePagerAdapter(
                supportFragmentManager,
                cacheManager.get(CacheManager.timeLine) as ArrayList<TimelineItem>
        )

        viewPager.adapter = adapter
        viewPager.currentItem = position
        viewPager.addOnPageChangeListener(this)

        Picasso.get()
                .load(deputy.photoUrl)
                .into(imageViewDeputy)

        textViewDeputyName.text = String.format("%s %s", deputy.firstname, deputy.lastname)
        textViewDeputyGroup.text = deputy.parliamentGroup
        textViewDeputyPlace.text = DeputyHelper.getFormattedLocality(resources, deputy)

        onPageSelected(position)

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        val timelineItem = adapter.getItemValue(position)
        updateDeputyVote(timelineItem)
    }

    private fun updateDeputyVote(item: TimelineItem) {

        val info = item.extraBallotInfo
        if (info == null) {
            deputyVoteView.visibility = View.INVISIBLE
        } else {
            deputyVoteView.visibility = View.VISIBLE
            val deputyVote = info.deputyVote
            deputyVoteView.setDeputyInfo(deputyVote?.voteValue)
        }

    }

    override fun onBackPressed() {
        val data = Intent()
        data.putExtra(EXTRA_POSITION, viewPager.currentItem)
        setResult(Activity.RESULT_OK, data)
        super.onBackPressed()
    }

}
