package ru.netology.nmedia.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.ImageAttachmentFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.PostViewModel

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
//        ownerProducer = ::requireParentFragment
    )

//    private val viewModel: PostViewModel by viewModels(
//        ownerProducer = ::requireParentFragment
//    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                val viewModelAuth by viewModels<AuthViewModel>()
                viewModelAuth.auth.observe(viewLifecycleOwner) {
                    if (viewModelAuth.isAuthorized) {
                        viewModel.likeById(post.id)
                    } else {
                        AlertDialog.Builder(context)
                            .setMessage(getString(R.string.like_post_dialog))
                            .setTitle(getString(R.string.like_post_dialog_header))
                            .setPositiveButton(getString(R.string.sign_in)) { dialog, which ->
                                findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
                            }
                            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                            }
                            .create()
                            .show()
                    }
                }
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onRetryLoad(post: Post) {
                viewModel.edit(post)
                viewModel.save()
            }

            override fun onOpenImageAttachment(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_imageAttachmentFragment,
                    Bundle().apply {
                        textArg = post.attachment?.url
                    })
            }
        })
        binding.list.adapter = adapter
        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) { viewModel.loadPosts() }
                    .show()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }
//        viewModel.data.observe(viewLifecycleOwner) { state ->
//            adapter.submitList(state.posts)
//            binding.emptyText.isVisible = state.empty
//        }

//        viewModel.newerCount.observe(viewLifecycleOwner) {
//            Log.d("FeedFragment", "Newer count: $it")
//            if (it > 0) {
//                binding.showNewerPosts.text = getString(R.string.showNewPosts, it)
//                binding.showNewerPosts.visibility = View.VISIBLE
//            } else {
//                binding.showNewerPosts.visibility = View.GONE
//            }
//
//        }

        binding.showNewerPosts.setOnClickListener {
            viewModel.showAll()
            binding.showNewerPosts.visibility = View.GONE
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        })

        lifecycleScope.launchWhenCreated {

            adapter.loadStateFlow.collectLatest {
                binding.swiperefresh.isRefreshing = it.refresh is LoadState.Loading
                        || it.append is LoadState.Loading
                        || it.prepend is LoadState.Loading
            }

        }


        binding.swiperefresh.setOnRefreshListener {
//            viewModel.refreshPosts()
            adapter.refresh()
        }

        binding.fab.setOnClickListener {

            val viewModel by viewModels<AuthViewModel>()
            viewModel.auth.observe(viewLifecycleOwner) {
                if (viewModel.isAuthorized) {
                    findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
                } else {
                    AlertDialog.Builder(context)
                        .setMessage(getString(R.string.create_post_dialog))
                        .setTitle(getString(R.string.create_post_dialog_header))
                        .setPositiveButton(getString(R.string.sign_in)) { dialog, which ->
                            findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
                        }
                        .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                        }
                        .create()
                        .show()
                }
            }
        }

        return binding.root
    }
}
