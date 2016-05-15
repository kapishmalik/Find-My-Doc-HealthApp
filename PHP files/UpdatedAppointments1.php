<?php
$response = array();
$appointmentDate = "2015-11-25";
$appointmentID   = 8;
$appointmentTime ="10:30:00";
$slotid          = 1;
$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql="UPDATE appointments set appointmenttime = '".$appointmentTime."',appointmentdate = '".$appointmentDate."',slotid = ".$slotid.",appointmentstatuscode=0 where appointmentid = ".$appointmentID."";
   $result = mysqli_query($connection,$sql);
   if ($result) {
    $response["success"] = 1;
    echo json_encode($response);
   }    
else
{
  $response["success"] = 0;
    echo json_encode($response);

}


?>
