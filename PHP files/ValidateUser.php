<?php
$response = array();
if (isset($_POST['email']) && isset($_POST['pwd'])&& isset($_POST['table'])){    
    $email =$_POST['email'];
    $pwd =md5($_POST['pwd']);
    $table=$_POST['table'];
    $doctor="doctor";
 $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -2;
     echo json_encode($response);
   }
$sql="select * from ".$table." where email ='".$email."'";
   $result = mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
    $row = mysqli_fetch_assoc($result);
    if($pwd == $row["password"])
   { 
    if($table==$doctor)
   {
     
     $sql1="SELECT * FROM qualificationdetails where 
      qualificationid = ".$row["qualificationid"]."";
     $result1=mysqli_query($connection,$sql1);
     $row1 = mysqli_fetch_assoc($result1);
     $response["name"]=$row["name"];
     $response["gender"]=$row["gender"];
     $response["age"]=$row["age"];
     $response["YrsOfExp"]=$row["yrsofexp"];
     $response["MCINo"]=$row["medicalcouncilno"];
     $response["ugDegree"]=$row1["ugdegree"];
     $response["pgDegree"]=$row1["pgdegree"];
     $response["otherDegree"]=$row1["otherdegree"];
     $response["speciality"]=$row1["speciality"];
     $response["success"] = 1;
     echo json_encode($response);
   }
   else
   {
     $response["name"]=$row["name"];
     $response["gender"]=$row["gender"];
     $response["age"]=$row["age"];
     $response["contact"]=$row["contactno"];
     $response["address"]=$row["address"];
     $response["success"] = 1;
     echo json_encode($response);
   }
   }    
   else
  { 
   $response["success"] = 0;
   echo json_encode($response);
  }
}
else
{
  $response["success"] = -1;
   echo json_encode($response);
}
mysqli_close($connection);
}
else
{
  $response["success"] = -2;
   echo json_encode($response);
}

?>