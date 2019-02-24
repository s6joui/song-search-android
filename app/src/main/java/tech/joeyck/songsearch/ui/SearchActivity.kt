package tech.joeyck.songsearch.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_search.*
import tech.joeyck.songsearch.R

class SearchActivity : AppCompatActivity() {

    companion object {
        const val TRACK_INDEX : String = "track_index"
        const val SEARCH_QUERY : String = "search_query"
    }

    private lateinit var searchViewModel : SearchViewModel
    private lateinit var adapter : ResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.elevation = 0f

        // RecyclerView setup
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayout.VERTICAL))

        adapter = ResultsAdapter(this){ selectedTrackIndex ->
            val i = Intent(this,PlayerActivity::class.java)
            i.putExtra(TRACK_INDEX,selectedTrackIndex)
            i.putExtra(SEARCH_QUERY,searchViewModel.searchQuery)
            startActivity(i)
        }
        recyclerView.adapter = adapter

        // Search input setup
        initSearchInputListener()

        // View model setup
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        searchViewModel.results().observe(this, Observer { results ->
            progress.visibility = View.INVISIBLE
            adapter.submitItems(results)
            recyclerView.scrollToPosition(0)
            if(results.isEmpty()){
                text_error.text = getString(R.string.no_results)
                text_error.visibility = View.VISIBLE
            }else{
                text_error.visibility = View.INVISIBLE
            }
        })

        searchViewModel.error().observe(this, Observer { errorMessage ->
            progress.visibility = View.INVISIBLE
            text_error.visibility = View.VISIBLE
            text_error.text = errorMessage
        })

    }

    private fun initSearchInputListener() {
        searchText.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        searchText.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }

    }

    private fun doSearch(v: View) {
        progress.visibility = View.VISIBLE
        text_error.visibility = View.INVISIBLE
        adapter.clearItems()
        dismissKeyboard(v.windowToken)
        val query = searchText.text.toString()
        searchViewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        searchViewModel.sortResultsBy(item?.itemId!!)
        return super.onOptionsItemSelected(item)
    }

}
