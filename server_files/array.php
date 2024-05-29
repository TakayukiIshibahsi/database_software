<?php
$total = 0;
$data = [98,76,54,32,19,87,65,43,21];
$n=count($data);
for($i=0;$i<$n;$i++) {
    $total+=$data[$i];
}
echo "result".$total;
