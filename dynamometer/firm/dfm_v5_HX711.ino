#include "HX711.h"
#include <SoftwareSerial.h>

// SoftwareSerial BTSerial(2, 3);

// Set Loadcell pins
#define LOADCELL_DOUT_PIN1 3 // hx-711 index
#define LOADCELL_SCK_PIN1 2
#define LOADCELL_DOUT_PIN2 5 // hx-711 middle
#define LOADCELL_SCK_PIN2 4
#define LOADCELL_DOUT_PIN3 7 // hx-711 ring
#define LOADCELL_SCK_PIN3 6
#define LOADCELL_DOUT_PIN4 9 // hx-711 little
#define LOADCELL_SCK_PIN4 8

class LoadCell {
  private:
    HX711 loadCell;
    String name;
    int digitalOut;
    int serialClockInput;
    long calibrationFactor = 115000;
    double value[5] = {0.0,0.0,0.0,0.0,0.0};
    double avgValue=0.0, sumValue=0.0;
    int errorCounter = 0;
  public :
    LoadCell(String name, int dt, int sck){
      this->name = name;
      this->digitalOut = dt;
      this->serialClockInput = sck;
      loadCell.begin(digitalOut, serialClockInput);
    }
    // HX711 is ready?
    bool isReady(){
      return this->loadCell.is_ready();      
    }
    // read 24bits AD value
    long read(){
      return this->loadCell.read();      
    }
    // returns an average reading; times = how many times to read(sum(read())/times)
    long readAverage(byte times=10) {
      return this->loadCell.read_average(times);      
    }
    // returns (read_average() - OFFSET), that is the current value without the tare weight; times = how many readings to do    
    double getValue(byte times=1){
      return this->loadCell.get_value(times);      
    }
    // returns get_value() divided by SCALE, that is the raw value divided by a value obtained via calibration
    // get unit value(getValue(times) / SCALE)
    float getUnits(byte times=1) {
      return this->loadCell.get_units(times);
      //return random(0,20);
    }
    // get the scale value
    float getScale(){
      return this->loadCell.get_scale();
      // return 0;
    }
    // get the Offset Value(offset adjust average_value)
    long getOffset(){
      return this->loadCell.get_offset();
      // return 0;
    }
    // set the OFFSET value for tare weight
    // Make allwance(offset) to determine net weight
    void tare(byte times=10){
      this->loadCell.tare(times);
    }
    // set the SCALE value; this value is used to convert the raw data to "human readable" data (measure units)
    // Set the scale value
    void setScale(float scale=1.f){
      this->loadCell.set_scale(scale);
    }
    // // set OFFSET, the value that's subtracted from the actual reading (tare weight)
    void setOffset(long offset=0){
      this->loadCell.set_offset(offset);
    }
    //  puts the chip into power down mode
    void powerDown(){
      this->loadCell.power_down();
    }
    // wakes up the chip after power down mode
    void powerUp(){
      this->loadCell.power_up();
    }    
    void setup(){
      Serial.println("["+this->name+"]:"+"Load Cell Setup");
      Serial.println("["+this->name+"]:"+"Initializing the scale");
      // Initialize library with data output pin, clock input pin and gain factor.
      // Channel selection is made by passing the appropriate gain:
      // - With a gain factor of 64 or 128, channel A is selected
      // - With a gain factor of 32, channel B is selected
      // By omitting the gain factor parameter, the library
      // default "128" (Channel A) is used here.
      Serial.println("["+this->name+"]:"+"Before setting up the scale:");
      Serial.print("["+this->name+"]:"+"read: \t\t");
      Serial.println(this->read());      // print a raw reading from the ADC
    
      Serial.print("["+this->name+"]:"+"read average: \t\t");
      Serial.println(this->readAverage(20));   // print the average of 20 readings from the ADC
    
      Serial.print("["+this->name+"]:"+"get value: \t\t");
      Serial.println(this->getValue(5));   // print the average of 5 readings from the ADC minus the tare weight (not set yet)
    
      Serial.print("["+this->name+"]:"+"get units: \t\t");
      Serial.println(this->getUnits(5), 1);  // print the average of 5 readings from the ADC minus tare weight (not set) divided
                // by the SCALE parameter (not set yet)
    
      if((int)this->getUnits(5) != 0){
        //this->setScale(this->getUnits(10));
        this->setScale(calibrationFactor);
      }
      this->tare();               // reset the scale to 0
      Serial.println("["+this->name+"]:"+"Calibration Factor:" + this->getScale() );
      Serial.println("["+this->name+"]:"+"After setting up the scale:");
      
      Serial.print("["+this->name+"]:"+"read: \t\t");
      Serial.println(this->read());                 // print a raw reading from the ADC
    
      Serial.print("["+this->name+"]:"+"read average: \t\t");
      Serial.println(this->readAverage(20));       // print the average of 20 readings from the ADC
    
      Serial.print("["+this->name+"]:"+"get value: \t\t");
      Serial.println(this->getValue(5));   // print the average of 5 readings from the ADC minus the tare weight, set with tare()
    
      Serial.print("["+this->name+"]:"+"get units: \t\t");
      Serial.println(this->getUnits(5), 1);        // print the average of 5 readings from the ADC minus tare weight, divided
                // by the SCALE parameter set with set_scale
      Serial.println("["+this->name+"]:"+"Readings:");
    }
    void compensationAvgValue(){
      sumValue = avgValue;
      if(sumValue >= 10.0/4.0 && sumValue < 17.0 / 4.0)
      {
        avgValue = 0.98*avgValue;
      }
      else if(sumValue >= 17.0/4.0 && sumValue < 25.0/4.0)
      {
        avgValue = 1.1*avgValue-0.025;
      }
      else if(sumValue >= 25.0/4.0 && sumValue < 30.0/4.0)
      {
        avgValue = 1.05*avgValue-0.02;
      }
      else if(sumValue >= 30.0/4.0 && sumValue < 35.0/4.0)
      {
        avgValue = 1.0*avgValue-0.0125;
      }
      else if(sumValue >= 35.0/4.0 && sumValue < 40.0/4.0)
      {
        avgValue = 1.105*avgValue-0.02;
      }
      else if(sumValue >= 40.0/4.0)
      {
        avgValue = 0.98*avgValue;
      }
      avgValue = 0.67 * avgValue - 0.3;
      avgValue = constrain(avgValue, 00.00, 20.00);      
    }
    // Low pass Filter & set the scale
    void lowPassFilter(long milliSecond)
    {      
      int cutoffFrequency = 10; // cutoff frequency 5~10 Hz
      double delta = milliSecond/1000.0;
      double lambda = 2*3.14*cutoffFrequency*delta;
      double firstParameter = lambda/(1+lambda);
      double secondParameter = 1/(1+lambda);    
      if (this->isReady())
      {
        if(name.equals("Index")){
          value[0] = this->getUnits(1); // *3;      // 1-3 // 2-10
        }else if (name.equals("Middle")){
          value[0] = this->getUnits(1); //*10;    // 1-12 // 2-8  
        }else if (name.equals("Ring")){
          value[0] = this->getUnits(1); //*20;   // 1-20 //2-6.8
        }else if (name.equals("Little")){  
          value[0] = this->getUnits(1); //*10;   // 1-10 //2-8 //
        }
        avgValue = firstParameter*value[0]+secondParameter*value[1];
        if((avgValue > 20) && (errorCounter == 0))
        {
          avgValue = value[1];
          errorCounter = 5;
        }
        else if(errorCounter > 0)
        {
          avgValue = value[1];
          errorCounter--;
        }
        value[1] = avgValue;
      }
    }
    void calibration()
    {
      if(avgValue > 1)
      {
        Serial.println("Calibration :" + name);
        this->setScale(calibrationFactor);
        this->tare();
      }
    }
    void send(long milliSecond)
    {
      int zero = 0;
      if(avgValue < 10)
      {
        //Serial1.print(zero);
        //Serial1.print(avgValue, 2);
        Serial.print(zero);
        Serial.print(avgValue, 2);
      }
      else
      {
        //Serial1.print(avgValue, 2);
        Serial.print(avgValue, 2);
      }
    }
    
    void process(long milliSecond)
    {
      this->lowPassFilter(milliSecond);
      // this->compensationAvgValue(); 
      avgValue = constrain(avgValue, 00.00, 40.00);
      this->send(milliSecond);
    }
};

LoadCell loadCellIndex("Index", LOADCELL_DOUT_PIN1,LOADCELL_SCK_PIN1);
LoadCell loadCellMiddle("Middle", LOADCELL_DOUT_PIN2,LOADCELL_SCK_PIN2);
LoadCell loadCellRing("Ring", LOADCELL_DOUT_PIN3,LOADCELL_SCK_PIN3);
LoadCell loadCellLittle("Little", LOADCELL_DOUT_PIN4,LOADCELL_SCK_PIN4);
//check the sensor gathering milliseconds
unsigned long thisMillis=0, lastMillis=0, deltaMillis=0; 

void setup() {
  Serial.begin(115200);
  //Serial.begin(9600);
  //Serial1.begin(115200);
  loadCellIndex.setup();
  loadCellMiddle.setup();
  loadCellRing.setup();
  loadCellLittle.setup();
}

void loop() {
  thisMillis = millis();
  if(thisMillis != lastMillis)
  {
    deltaMillis = thisMillis - lastMillis;
    lastMillis = thisMillis;
  }    
  //Serial1.print('I');
  Serial.print('I');
  loadCellIndex.process(deltaMillis);
  //Serial1.print('M');
  Serial.print('M');
  loadCellMiddle.process(deltaMillis);
  //Serial1.print('R');
  Serial.print('R');
  loadCellRing.process(deltaMillis);
  //Serial1.print('L');
  Serial.print('L');
  loadCellLittle.process(deltaMillis);  
  //Serial1.print('T');
  Serial.print('T');
  //Serial1.print(deltaMillis/1000.0, 3);  
  Serial.print(deltaMillis/1000.0, 3);  
  //Serial.println();  
  Serial.print(';');
  delay(30);
  //Serial1.println();  
  Serial.println();  
  //Serial.println(deltaMillis/100.0, 2);  
  //Serial1.println(deltaMillis/100.0, 2);

  //if (Serial1.available() > 0) {
  //    char data;
  //    data = Serial1.read();
  //    if(data == 'I') {
  //      loadCellIndex.calibration();
  //      loadCellMiddle.calibration();
  //      loadCellRing.calibration();
  //      loadCellLittle.calibration();  
  //    }
  //}
}
