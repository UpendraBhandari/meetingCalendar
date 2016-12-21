package org.marketlogic.meetingCalender

import java.util._
import org.joda.time._
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class ParseFile {
  
  var wrkhrs = Array[String]()
  var firstLine= ""
  val dateFormatSubmit = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
  val dateFormatMeeting = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
  var linectr = 0 // counter to separate submission
  var submitEntries = Array[String]()
  
  var submissionrequest = scala.collection.mutable.Map[LocalDateTime,Array[String]]()
  //case class meetSubmit (submitDate: LocalDateTime, empID: String, meetStartDateTime: LocalDateTime, meetEndDateTime: LocalDateTime)
   
  def scanFile(scnr: Scanner): scala.collection.mutable.ArrayBuffer[meetSubmit] = {
    
    
    var submissionEntries  = scala.collection.mutable.ArrayBuffer[meetSubmit]()
    
    try {
    // Parse the Input file. iterate over the file, line by line
    while(scnr.hasNextLine()){
      
      var tempLine = scnr.nextLine();
      linectr = linectr + 1
      
      // first line is office hrs. put working hours inside an array
      if (firstLine.isEmpty()){
        firstLine = tempLine
        wrkhrs ++= Array(firstLine.split(" ")(0))
        wrkhrs ++= Array(firstLine.split(" ")(1))
        // println("Office Hours fetched")        
      }
      
      //each meeting entry is divided into 2 lines, line 1 indicates submission date and employee id, line 2 start date and meeting duration
      //logic for line 1
      if (linectr%2==0){
        val submissionDateTime = dateFormatSubmit.parseLocalDateTime(Arrays.copyOf(tempLine.split(" "),2).mkString(" "))
        val empID  = Arrays.copyOf(tempLine.split(" "),3)(2)
        //println(submissionDateTime, empID)
        submitEntries ++= Array(submissionDateTime.toString,empID)       
        
      }//logic for line 2
      else if (linectr!= 1 && linectr%2!=0 && !firstLine.isEmpty()){
        val meetingStartDateTime = dateFormatMeeting.parseLocalDateTime(Arrays.copyOf(tempLine.split(" "),2).mkString(" "))
        val meetingEndDateTime = dateFormatMeeting.parseLocalDateTime(Arrays.copyOf(tempLine.split(" "),2).mkString(" ")).
        plusHours(Arrays.copyOf(tempLine.split(" "),3)(2).toInt)
        
        //println(meetingStartDateTime, meetingEndDateTime)
        submitEntries ++= Array(meetingStartDateTime.toString,meetingEndDateTime.toString)
        
      }   
      
      // when 4 entries are pushed in  submitEntries array, it indicates end of 1 meeting request
      if (submitEntries.length==4){
        //println(submitEntries.mkString(" "))
        submissionrequest ++= scala.collection.Map(new LocalDateTime(submitEntries(0)) -> 
                    Array(submitEntries(1).toString,
                        submitEntries(2).toString,submitEntries(3).toString))
        
        // create ArrayBuffer of meetSubmit entries           
        var entry = scala.collection.mutable.ArrayBuffer(meetSubmit(new LocalDateTime(submitEntries(0)), submitEntries(1).toString, new LocalDateTime(submitEntries(2)), new LocalDateTime(submitEntries(3))))
        submissionEntries ++= entry
        
        submitEntries = Array[String]()      
      }       
    }
    }catch{
      case ex: Exception => {
        println("Unable to parse file..Error in input file")
        System.exit(1)
      }
    }
    return submissionEntries
    
    
  }
  
  
}