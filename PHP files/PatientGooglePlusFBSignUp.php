<?php
$response = array();
if (isset($_POST['email']) && isset($_POST['name'])&& isset($_POST['gender'])&& isset($_POST['age'])){    
    $email  =$_POST['email'];
    $name   =$_POST['name'];
    $gender =$_POST['gender'];
    $age    =(int)$_POST['age'];
$connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -1;
     echo json_encode($response);
   }
$sql="INSERT INTO patient (email,name,age,gender) values
('".$email."','".$name."',".$age.",'".$gender."')";
   $result = mysqli_query($connection,$sql);
   if ($result) {
    $response["success"] = 1;
    echo json_encode($response);
   }    
   else
  { 
   $sql="SELECT * from patient where email = '".$email."'";
   $result=mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
    $row = mysqli_fetch_assoc($result);
    $response["success"]=0;
    $response["email"]=$row["email"];
    $response["name"]=$row["name"];
    $response["age"]=$row["age"];
    $response["address"]=$row["address"];
    $response["contact"]=$row["contactno"];
    $response["password"]=$row["password"];
    $response["email"]=$row["email"];
    echo json_encode($response);
   }
   else
  {
     $response["success"] = -1;
     echo json_encode($response);

  }
        
}
mysqli_close($connection);
}
else
{
  $response["success"] = -1;
   echo json_encode($response);
}

?>
