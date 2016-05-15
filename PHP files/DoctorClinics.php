<?php
$response = array();
if (isset($_POST['email'])){    
    $email =$_POST['email'];
 $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -2;
     echo json_encode($response);
   }
$sql="select * from clinicrdoc where docemail ='".$email."'";
   $result = mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
   $response["clinics"] = array();
    while($row = mysqli_fetch_assoc($result))   
    {
     $sql1="SELECT * FROM clinicdetails where 
      clinicid = ".$row["clinicid"]."";
     $result1=mysqli_query($connection,$sql1);
     
	while($row1 = mysqli_fetch_assoc($result1))
	{
	     $clinicsarr = array();
	     $clinicsarr["clinicsarr"] = $row1["name"];
	     array_push($response["clinics"], $clinicsarr);
	}
     }
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
  $response["success"] = -2;
   echo json_encode($response);
}

?>

	