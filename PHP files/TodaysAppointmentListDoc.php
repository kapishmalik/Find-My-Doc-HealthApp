<?php
$response = array();
if (isset($_POST['email']) && isset($_POST['clinic_name'])){    
    $email =$_POST['email'];
    $clinic_name =$_POST['clinic_name'];
    
 $connection = mysqli_connect('localhost', '1046389', 'kapish1234', '1046389');
    if(!$connection)
   { 
     $response["success"] = -2;
     echo json_encode($response);
   }
   $result2 = 0;
   $sql="select * from clinicrdoc where docemail ='".$email."'";
   $result = mysqli_query($connection,$sql);
   if ($result->num_rows > 0) {
   while($row = mysqli_fetch_assoc($result))   
   {
     	   $sql1="SELECT * FROM clinicdetails where 
        clinicid = ".$row["clinicid"]." and name ='".$clinic_name."'";
     
        $result1=mysqli_query($connection,$sql1);
        if ($result1->num_rows > 0) {
	       $row1 = mysqli_fetch_assoc($result1);
            $id = $row1["clinicid"];
	       $sql2="select * from appointments where docemail ='".$email."' and clinicid ='".$id."' and appointmentstatuscode=1 and appointmentdate = '" . date("Y-m-d") . "'";
	       $result2 = mysqli_query($connection,$sql2);
	       $response["appointments"] = array();
	       while($row2 = mysqli_fetch_assoc($result2))
	       {
	           $apparr = array();
	           $apparr["appointmentdate"] = $row2["appointmentdate"];
	           $apparr["appointmenttime"] = $row2["appointmenttime"];
	           $apparr["purpose"] = $row2["purpose"];
			$sql4="select * from clinicdetails where clinicid ='".$row2["clinicid"]."' ";
			$result4=mysqli_query($connection,$sql4);
			$row4 = mysqli_fetch_assoc($result4);
			$apparr["clinicname"]=$row4["name"];
			$apparr["patname"]=$row2["patientemail"];	
	           array_push($response["appointments"], $apparr);
	       }           
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

		

