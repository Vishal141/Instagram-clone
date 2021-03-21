package com.example.instaclone.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.instaclone.Models.Global
import com.example.instaclone.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add.*
import java.io.File
import java.lang.Exception

class AddFragment : Fragment() {

    var imageUri:Uri?=null
    var myurl:String?=null
    var storageReference:StorageReference?=null

    var imageView:ImageView?=null
    var description:EditText?=null
    var postBtn:Button?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root:View = inflater.inflate(R.layout.fragment_add, container, false)

        imageView = root.findViewById(R.id.selectedImage);
        description = root.findViewById(R.id.description);
        postBtn = root.findViewById(R.id.post)

        storageReference = FirebaseStorage.getInstance().getReference("Posts")

        CropImage.activity().setAspectRatio(1,1).start(context!!,this)

        postBtn!!.setOnClickListener(View.OnClickListener {
            uploadImage()
        })

        return root
    }

    private fun getFileExtension(uri:Uri):String{
        var contentResolver:ContentResolver = activity!!.contentResolver
        if(uri.scheme==ContentResolver.SCHEME_FILE)
            return File(uri.path!!).extension
        var mimeType:MimeTypeMap = MimeTypeMap.getSingleton()
        println(mimeType)
        return  mimeType.getExtensionFromMimeType(contentResolver.getType(uri))!!
    }

    private fun uploadImage(){
        var pd:ProgressDialog = ProgressDialog(context)
        pd.setMessage("posting..")
        pd.show()

        if(imageUri!=null){
            var path:String = System.currentTimeMillis().toString() + "." + getFileExtension(imageUri!!)
            var fileReference:StorageReference = storageReference!!.child(path)

            fileReference.putFile(imageUri!!).continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>>{
                override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                    if(!p0.isComplete)
                        throw p0.exception!!
                    return fileReference.downloadUrl
                }
            }).addOnCompleteListener(object :OnCompleteListener<Uri>{
                override fun onComplete(p0: Task<Uri>) {
                    if(p0.isSuccessful){
                        var downloadUri:Uri = p0.result!!
                        myurl = downloadUri.toString()

                        var databaseReference:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Posts")

                        var postId:String = databaseReference.push().key!!
                        var map = HashMap<String,String>()
                        map.put("postId",postId)
                        map.put("postImage",myurl!!)
                        map.put("description",description!!.text.toString())
                        map.put("publisher",Global.currentUserId)

                        databaseReference.child(postId).setValue(map)

                        pd.dismiss()

                        (context as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()

                    }else{
                        Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show()
                    }
                }
            }).addOnFailureListener(object:OnFailureListener{
                override fun onFailure(p0: Exception) {
                    Toast.makeText(context,""+p0.localizedMessage,Toast.LENGTH_LONG).show()
                }
            })
        }else{
            Toast.makeText(context,"No image selected",Toast.LENGTH_LONG)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            var result:CropImage.ActivityResult = CropImage.getActivityResult(data)
            imageUri = result.uri

            imageView!!.setImageURI(imageUri)
        }else{
            Toast.makeText(context,"something went wrong",Toast.LENGTH_LONG).show()
            (context as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()
        }

    }
}