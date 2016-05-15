<?php
$response = array();
if (isset($_POST['appointmentid'])){    
    $appointmentid =$_POST['appointmentid'];
    $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql="delete from appointments where appointmentid =".$appointmentid."";
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
