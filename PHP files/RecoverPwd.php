<?php
$response = array();
if (isset($_POST['email']) && isset($_POST['table'])){    
    $email =$_POST['email'];
    $table=$_POST['table'];
    $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql="select * from ".$table." where email ='".$email."'";
   $result = mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
     $row = mysqli_fetch_assoc($result);
     $response["success"] = 1;
     $response["password"]= $row["password"];
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
