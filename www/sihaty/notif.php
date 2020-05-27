<?php 
require('../sihaty/ConnectDB.php');
    $result = $conn->query("SELECT COUNT(*) FROM patients WHERE isConfirmed=0");
    while($cmp = $result->fetch()){
        $donnee = $cmp["COUNT(*)"];
    }

		$result = $conn->query("SELECT COUNT(*) FROM rendezvous WHERE isConfirmed=0");
    while($cmp = $result->fetch(PDO::FETCH_ASSOC)){
        $donnee += $cmp["COUNT(*)"];
    }
?>

Sihaty <?php if($donnee>0) echo '('.$donnee.')'; ?>
