<?php
require('../sihaty/ConnectDB.php');
    $result = $conn->query("SELECT COUNT(*) FROM patients WHERE isConfirmed=0");
    while($cmp = $result->fetch()){
        $response[] = $cmp;
    }
foreach($response as $donnee)
{
	if($donnee["COUNT(*)"]>0)
	{
?>
	<center>
		<div id="notification">
			<?php echo $donnee["COUNT(*)"]; ?>
		</div>
	</center>
<?php 
	}
}
?>