<?php
$response = array();
if (isset($_POST['email']))
{    
    $email  =$_POST['email'];
    
$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql="SELECT * FROM doctor WHERE email = '".$email."'";
   $result = mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
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
