package com.example.aplicaiciondeimc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent


import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.aplicaiciondeimc.conexion.*
/*import com.aristidevs.androidmaster.R*/
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.text.DecimalFormat
import java.util.Date

class imcCalculator : AppCompatActivity() {

    private var isMaleSelected: Boolean = true
    private var isFemaleSelected: Boolean = false
    private var currentWeight: Int = 70
    private var currentAge: Int = 30
    private var currentHeight: Int = 120

    private lateinit var viewMale: CardView
    private lateinit var viewFemale: CardView
    private lateinit var tvHeight: TextView
    private lateinit var rsHeight: RangeSlider
    private lateinit var btnSubtractWeight: FloatingActionButton
    private lateinit var btnPlusWeight: FloatingActionButton
    private lateinit var tvWeight: TextView
    private lateinit var btnSubtractAge: FloatingActionButton
    private lateinit var btnPlusAge: FloatingActionButton
    private lateinit var tvAge: TextView
    private lateinit var btnCalculate: Button

    companion object{
        const val IMC_KEY = "IMC_RESULT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc_calculator)
        initComponents()
        initListeners()
        initUI()


    }

    private fun initComponents() {
        viewMale = findViewById(R.id.viewMale)
        viewFemale = findViewById(R.id.viewFemale)
        tvHeight = findViewById(R.id.tvHeight)
        rsHeight = findViewById(R.id.rsHeight)
        btnSubtractWeight = findViewById(R.id.btnSubtractWeight)
        btnPlusWeight = findViewById(R.id.btnPlusWeight)
        tvWeight = findViewById(R.id.tvWeight)
        btnSubtractAge = findViewById(R.id.btnSubtractAge)
        btnPlusAge = findViewById(R.id.btnPlusAge)
        tvAge = findViewById(R.id.tvAge)
        btnCalculate = findViewById(R.id.btnCalculate)

    }

    private fun initListeners() {
        viewMale.setOnClickListener {
            changeGender()
            setGenderColor()
        }
        viewFemale.setOnClickListener {
            changeGender()
            setGenderColor()
        }
        rsHeight.addOnChangeListener { _, value, _ ->
            val df = DecimalFormat("#.##")
            currentHeight = df.format(value).toInt()
            tvHeight.text = "$currentHeight cm"
        }
        btnPlusWeight.setOnClickListener {
            currentWeight += 1
            setWeight()
        }
        btnSubtractWeight.setOnClickListener {
            currentWeight -= 1
            setWeight()
        }
        btnPlusAge.setOnClickListener {
            currentAge += 1
            setAge()
        }
        btnSubtractAge.setOnClickListener {
            currentAge -= 1
            setAge()
        }
        btnCalculate.setOnClickListener {
            val result = calculateIMC()
            navigateToResult(result)

        }


    }

    private fun navigateToResult(result: Double) {
        val intent = Intent(this, ResultIMCActivity::class.java)
        intent.putExtra(IMC_KEY, result)
        startActivity(intent)
    }

    //@SuppressLint("SuspiciousIndentation")
    private fun calculateIMC():Double {
        val df = DecimalFormat("#.##")
        val imc = currentWeight / (currentHeight.toDouble() /100 * currentHeight.toDouble()/100)
        Log.i("Gabriel","Mensaje")
        conectar()
      // funciones.OracleDBOperations.insertIMCData(conexion.OracleDBConnection.getConnection(),currentHeight.toDouble(),currentWeight,if (isMaleSelected) "Male" else "Female",currentAge)
     //val conectardb= OracleDBConnection.getConnection()
       //funciones.OracleDBOperations.insertIMCData(conectardb,currentHeight.toDouble(), currentWeight, if (isMaleSelected) "Male" else "Female", currentAge)

        return df.format(imc).toDouble()
    }

    private fun setAge() {
        tvAge.text = currentAge.toString()
    }

    private fun setWeight() {
        tvWeight.text = currentWeight.toString()
    }

    private fun changeGender() {
        isMaleSelected = !isMaleSelected
        isFemaleSelected = !isFemaleSelected
    }

    private fun setGenderColor() {
        viewMale.setCardBackgroundColor(getBackgroundColor(isMaleSelected))
        viewFemale.setCardBackgroundColor(getBackgroundColor(isFemaleSelected))
    }

    private fun getBackgroundColor(isSelectedComponent: Boolean): Int {

        val colorReference = if (isSelectedComponent) {
            R.color.background_component_selected
        } else {
            R.color.background_component
        }

        return ContextCompat.getColor(this, colorReference)
    }


    private fun initUI() {
        setGenderColor()
        setWeight()
        setAge()
    }
    fun conectar(){
        Log.i("Gabriel","Llega bien")
        GlobalScope.launch(Dispatchers.IO){
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver")
                val jdbcUrl= "jdbc:oracle:thin:@192.168.1.141:1521:XE"
                val usuario ="SYSTEM"
                val contrasena ="1234"
                Log.i("Gabriel","hastaca")
                var conexion:Connection=DriverManager.getConnection(jdbcUrl,usuario ,contrasena)
                Log.i("Gabriel","para ver")
                var statement:Statement=conexion.createStatement()
                Log.i("Gabriel","Paso Bien ya esta hecha la conexion")
                val icm=calculateIMC()
                var resultSet: ResultSet = statement.executeQuery("INSERT INTO IMC_DATOS(icm,altura_IMC,peso,edad,sexo) VALUES("+icm+","+currentHeight+","+currentWeight+",,"+currentAge+","+ if (isMaleSelected) "masculino" else "Femenino"+")")

                resultSet.next()


               conexion.close()
                GlobalScope.launch(Dispatchers.Main) {

                }

            } catch (e: Exception){
                Log.i("Gabriel","Error: "+e.toString())

            }
        }
    }





}