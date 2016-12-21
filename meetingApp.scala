package org.marketlogic.meetingCalender



/*
 * 
 * Author: Upendra Bhandari
 * Application : Meeting Calendar
 * Version: 0.1
 * Scala Version: 2.10.4 
 * 
 * 
 * This system assumes data to be input in form of flat file. If Flat file cannnot be parsed, all the input is discarded. 
 * 
 * TODO
 * System should ignore entries which cannot be be parsed, not the entire file.
 * Hours and Minutes should be of format HH and mm for single digit timings.
 * 
 */


import org.joda.time._
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.util.Arrays
import java.io.File
import java.util.Scanner;
import scala.collection.mutable._

// case class for meet submission entry
case class meetSubmit (submitDate: LocalDateTime, empID: String, meetStartDateTime: LocalDateTime, meetEndDateTime: LocalDateTime)
 
   
object meetingApp { 
  
  def main(args: Array[String])=  {
    
    if (args.length == 0){
      println("Please provide full path to file")
      println("Usage: sbt \"run <full path to file>\" ")
      System.exit(1)
    }
    
    // read file passed by the user
    val fileLoc = args(0).toString()
    val filePtr = new File(fileLoc)
    val scnr = new Scanner(filePtr)
    
    // data structures to store parsed file data    
    var wrkhrs = Array[String]()
    var firstLine= ""
    val dateFormatSubmit = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    val dateFormatMeeting = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
    var linectr = 0 // counter to separate submission
    var submitEntries = Array[String]()
    
    // Parse the input file
    val parseFile = new ParseFile()
       
    // Perform Validations
    val validationTimings = new ValidationLogic()
    validationTimings.submissionEntries = parseFile.scanFile(scnr)
    validationTimings.submissionrequest = parseFile.submissionrequest
    
    // remove meeting entries which are out of hours
    validationTimings.outOfOfficeTiming(parseFile.wrkhrs) 
    // remove entries which have overlap meeting hours
    validationTimings.overlapTiming()
    
    var submissionRequest = validationTimings.submissionrequest
    var submissionEntries = validationTimings.submissionEntries
    
    
    // Display Logic
    val entryDisplay = new DisplayLogic()
    entryDisplay.display(submissionEntries)
    
    
    
    
        
  }
  
  
}