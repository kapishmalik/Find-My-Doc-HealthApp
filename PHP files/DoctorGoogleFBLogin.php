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
    $row = mysqli_fetch_assoc($result);
    $sql="SELECT * FROM qualificationdetails WHERE qualificationid = ".$row["qualificationid"]." ";
    $result1=mysqli_query($connection,$sql);
    if($result1->num_rows > 0)
   { 
    $row1 = mysqli_fetch_assoc($result1);
    $response["success"] = 1;
    $response["password"]= $row["password"];
    $response["YrsOfExp"]= $row["yrsofexp"];
    $response["MCINo"]   = $row["medicalcouncilno"];
    $response["ugDegree"]   = $row1["ugdegree"];
    $response["pgDegree"]   = $row1["pgdegree"];
    $response["otherDegree"]= $row1["otherdegree"];
    $response["speciality"] = $row1["speciality"];
    echo json_encode($response);
   }
   else
  { 
    $response["success"] = -1;
    echo json_encode($response);
   }
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
