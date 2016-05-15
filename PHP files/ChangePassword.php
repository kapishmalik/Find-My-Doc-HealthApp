<?php
$response = array();
    $email     = $_POST['email'];
    $pwd       = md5($_POST['pwd']);
    $table     = $_POST['table'];
 
   $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
   $sql="UPDATE ".$table." set password = '".$pwd."' where email = '".$email."'";

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

