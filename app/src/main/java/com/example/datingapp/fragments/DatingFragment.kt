package com.example.datingapp.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.customtabs.IPostMessageService.Default
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.datingapp.R
import com.example.datingapp.adapter.DatingAdapter
import com.example.datingapp.databinding.FragmentDatingBinding
import com.example.datingapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DatingFragment : Fragment() {
    private lateinit var binding : FragmentDatingBinding
    private lateinit var manager : CardStackLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatingBinding.inflate(layoutInflater)
        getData()
        return binding.root
    }

    private fun init(){
        manager = CardStackLayoutManager(requireContext(), object :CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }
            override fun onCardSwiped(direction: Direction?) {
                if(manager.topPosition == list!!.size){
                    Toast.makeText(requireContext(),"This is the last card",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCardRewound() {

            }
            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)

    }
    companion object{
         var list : ArrayList<UserModel>? = null
    }

    private fun getData() {
        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("SHUBH", "onDataChange, ${snapshot.toString()}")
                    if(snapshot.exists()){
                        list = arrayListOf<UserModel>()
                        for (data in snapshot.children){
                            val model = data.getValue(UserModel::class.java)
                            if(model!!.number != FirebaseAuth.getInstance().currentUser!!.phoneNumber) {
                                list!!.add(model)
                            }
                        }
                        list!!.shuffle()
                        init()

                        binding.cardStackView.layoutManager = manager
                        binding.cardStackView.itemAnimator = DefaultItemAnimator()
                        binding.cardStackView.adapter = DatingAdapter(requireContext(),list!!)
                    }else{
                        Toast.makeText(requireContext(),"Something went Wrong",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }

            })


    }


}