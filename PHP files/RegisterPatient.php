<?php
$response = array();
if (isset($_POST['email']) && isset($_POST['pwd'])&& isset($_POST['name'])&& isset($_POST['address'])&& isset($_POST['gender'])&& isset($_POST['contact'])&& isset($_POST['age'])){    
    $email  =$_POST['email'];
    $pwd    =md5($_POST['pwd']);
    $name   =$_POST['name'];
    $address=$_POST['address'];
    $contact=$_POST['contact'];
    $gender =$_POST['gender'];
    $age    =(int)$_POST['age'];
$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql="INSERT INTO patient values('".$email."','".$name."','".$contact."','".$pwd."','".$address."',".$age.",'".$gender."')";
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
mysqli_close($connection);
}
else
{
  $response["success"] = -1;
   echo json_encode($response);
}

?>
