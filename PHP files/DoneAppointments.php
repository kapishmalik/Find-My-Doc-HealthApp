<?php
$response = array();
if(isset($_POST['appointmentdate']) && isset($_POST['appointmentslot']))
{    
   $date  =$_POST['appointmentdate'];
   $time  =$_POST['appointmentslot'];
   $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');

    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql="UPDATE appointments SET appointmentstatuscode=3 WHERE  appointmentdate = '".$date."' and appointmenttime = '".$time."'";
    if (mysqli_query($connection,$sql)){
     $response["success"] = 1;
     echo json_encode($response);
   }
   else
   {
      $response["success"] = 0;
      echo json_encode($response);
}
mysqli_close($connection);
}
else
{
  $response["success"] = -1;
   echo json_encode($response);
}
?>

