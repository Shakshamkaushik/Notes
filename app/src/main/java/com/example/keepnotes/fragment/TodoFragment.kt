package com.example.keepnotes.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepnotes.R
import com.example.keepnotes.adapter.TodoAdapter
import com.example.keepnotes.adapter.iTodo
import com.example.keepnotes.broadcast.AlarmReceiver
import com.example.keepnotes.broadcast.channelID
import com.example.keepnotes.broadcast.notificationID
import com.example.keepnotes.databinding.FragmentTodoBinding
import com.example.keepnotes.model.Todo
import com.example.keepnotes.viewmodel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Calendar

class TodoFragment : Fragment(), iTodo {

    lateinit var binding: FragmentTodoBinding
    lateinit var viewModel: NotesViewModel
    var isFirstTimeCreate: Boolean = false
    var isFromGetTimes: Boolean = false
    lateinit var btnsheet: View
    lateinit var dialog: BottomSheetDialog

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnsheet = layoutInflater.inflate(R.layout.todo_bottom_sheet_layout, null)
        dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(btnsheet)
        createNotificationChannel()
        btnsheet.findViewById<Button>(R.id.submitButton).setOnClickListener {
            Log.d("TAG", "SUBMIT BUTTON CLICK")
            if (checkNotificationPermissions(requireActivity())) {
                // Schedule a notification
                scheduleNotification()
                dialog.dismiss()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(NotesViewModel::class.java)
        isFirstTimeCreate = true


        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    dialog.dismiss()
                    btnsheet.findViewById<LinearLayout>(R.id.enterNote).visibility = View.VISIBLE
                    btnsheet.findViewById<LinearLayout>(R.id.setTime).visibility = View.GONE
                }
            })


        binding.todoFloatingButton.setOnClickListener {
            btnsheet.findViewById<LinearLayout>(R.id.enterNote).visibility = View.VISIBLE
            btnsheet.findViewById<LinearLayout>(R.id.setTime).visibility = View.GONE
            dialog.show()
            btnsheet.findViewById<TextView>(R.id.setAlarm).setOnClickListener {
                btnsheet.findViewById<LinearLayout>(R.id.enterNote).visibility = View.GONE
                btnsheet.findViewById<LinearLayout>(R.id.setTime).visibility = View.VISIBLE
            }

            btnsheet.findViewById<TextView>(R.id.tvDone).setOnClickListener {
                Log.d("TAG", " INSIDE TVDONE")
                val et1 = btnsheet.findViewById<EditText>(R.id.etTodo).text.toString()

                if (et1.isNotEmpty()) {
                    viewModel.addTodoNote(Todo(et1))
                }
                btnsheet.findViewById<EditText>(R.id.etTodo).text.clear()
                dialog.dismiss()

            }
        }


        binding.todoRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = TodoAdapter(requireActivity(), this)
        binding.todoRv.adapter = adapter


        viewModel.allTodoNotes.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.updateList(it as ArrayList<Todo>)
            }
        }


        return binding.root
    }

    override fun onLongItemClick(position: Int, todo: Todo) {

        binding.todoDelLl.visibility = View.VISIBLE
        binding.todoFloatingButton.visibility = View.GONE

        binding.ivTodoDel.setOnClickListener {
            viewModel.deleteTodoNote(todo)
            binding.todoDelLl.visibility = View.GONE
            binding.todoFloatingButton.visibility = View.VISIBLE
        }

    }

    override fun onItemClick(position: Int, todo: Todo) {
        isFirstTimeCreate = false
        btnsheet.findViewById<LinearLayout>(R.id.enterNote).visibility = View.VISIBLE
        btnsheet.findViewById<LinearLayout>(R.id.setTime).visibility = View.GONE
        dialog.dismiss()
        if (binding.todoDelLl.visibility == View.GONE) {
            showBottomSheet(todo, isFirstTimeCreate)
        } else {
            binding.todoDelLl.visibility = View.GONE
            binding.todoFloatingButton.visibility = View.VISIBLE
        }


    }

    private fun showBottomSheet(todo: Todo, isFirstTimeCreate: Boolean) {
        btnsheet.findViewById<TextView>(R.id.setAlarm).setOnClickListener {
            btnsheet.findViewById<LinearLayout>(R.id.enterNote).visibility = View.GONE
            btnsheet.findViewById<LinearLayout>(R.id.setTime).visibility = View.VISIBLE
        }

        val etTodo = btnsheet.findViewById<EditText>(R.id.etTodo)

        etTodo.setText(todo.todoTask)
        if (!isFirstTimeCreate) {
            btnsheet.findViewById<TextView>(R.id.tvDone).setOnClickListener {
                viewModel.updateTodoNote(Todo(etTodo.text.toString(), todo.id))
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification() {
        // Create an intent for the Notification BroadcastReceiver
        val intent = Intent(requireActivity().applicationContext, AlarmReceiver::class.java)

        val todoTask = btnsheet.findViewById<EditText>(R.id.etTodo).text.toString()
        intent.putExtra("todoTask", todoTask)
        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            requireActivity().applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Get the AlarmManager service
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Get the selected time and schedule the notification
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        // Show an alert dialog with information
        // about the scheduled notification
       Toast.makeText(requireContext(),"Todo Alarm Set Successfully",Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("CutPasteId")
    private fun getTime(): Long {
        // Get selected time from TimePicker and DatePicker
        val timePicker = btnsheet.findViewById<TimePicker>(R.id.timePicker)
        val datePicker = btnsheet.findViewById<DatePicker>(R.id.datePicker)
        val minute = timePicker.minute
        val hour = timePicker.hour
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        // Create a Calendar instance and set the selected date and time
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)


        return calendar.timeInMillis
    }

//    private fun showAlert(time: Long, title: String, message: String) {
//        // Format the time for display
//        val date = Date(time)
//        val dateFormat =
//            android.text.format.DateFormat.getLongDateFormat(requireActivity().applicationContext)
//        val timeFormat =
//            android.text.format.DateFormat.getTimeFormat(requireActivity().applicationContext)
//
//        // Create and show an alert dialog with notification details
//        AlertDialog.Builder(requireActivity())
//            .setTitle("Notification Scheduled")
//            .setMessage(
//                "Title: $title\nMessage: $message\nAt: ${dateFormat.format(date)} ${
//                    timeFormat.format(
//                        date
//                    )
//                }"
//            )
//            .setPositiveButton("Okay") { _, _ -> }
//            .show()
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        // Create a notification channel for devices running
        // Android Oreo (API level 26) and above
        val name = "Notify Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        // Get the NotificationManager service and create the channel
        val notificationManager =
            requireActivity().getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkNotificationPermissions(context: Context): Boolean {
        // Check if notification permissions are granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val isEnabled = notificationManager.areNotificationsEnabled()

            if (!isEnabled) {
                // Open the app notification settings if notifications are not enabled
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(intent)

                return false
            }
        } else {
            val areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()

            if (!areEnabled) {
                // Open the app notification settings if notifications are not enabled
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(intent)

                return false
            }
        }

        // Permissions are granted
        return true
    }

}

