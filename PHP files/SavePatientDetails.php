<?php
$response = array();
    $email     = $_POST['email'];
    $name      = $_POST['name'];
    $address   = $_POST['address'];
    $contact   = $_POST['contact'];
    $gender    = $_POST['gender'];
    $age       = $_POST['age'];
    $prevemail = $_POST['prevemail'];
$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
   $sql="UPDATE patient set name = '".$name."',email = '".    $email."',contactno = '".$contact."',address = '".$address."',gender ='".$gender."',age = ".$age." where email = '".$prevemail."'";
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

